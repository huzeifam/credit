package com.example.credit.controller;


import com.example.credit.model.CreditCreateRequest;
import com.example.credit.model.CreditResponse;
import com.example.credit.model.CreditResponseEnum;
import com.example.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Autowired
    RestTemplate restTemplate;

    @Operation(summary = "Display all credits")
    @GetMapping("/credits")
    public List<CreditResponse> getAllCredits() {
        return creditService.findAllCredits();
    }

    @Operation(summary = "Get credit by credit number")
    @GetMapping("/credits/{creditNo}")
    public ResponseEntity<Object> getCreditByCreditNo(
            @Parameter(description = "Credit number of credit to display")
            @PathVariable Integer creditNo
    ) {
        Optional<CreditResponse> credit = creditService.findByCreditNo(creditNo);
        if (credit.isPresent())
            return ResponseEntity.ok(credit.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit (credit number: " + creditNo + ") does not exist");
    }

    @Hidden
    @GetMapping("/credits/account-credit/{accountNo}")
    public Object[] getCreditByAccountNo(
            @PathVariable Integer accountNo
    ) {
        List<CreditResponse> credit = creditService.findCreditByAccountNo(accountNo);
        if (credit.isEmpty())
            return null;
        else
            return new List[]{credit};
    }

    @Operation(summary = "Request credit")
    @PostMapping("/credits/{accountNo}/{creditAmount}")
    public ResponseEntity<String> requestCredit(
            @Parameter(description = "Account number of account to request credit")
            @PathVariable Integer accountNo,
            @Parameter(description = "Amount of credit")
            @PathVariable Double creditAmount,
            @Parameter(description = "Set period (in month) for credit")
            @RequestParam CreditResponseEnum enumRequest
    ) {
        Integer creditNo = UUID.randomUUID().hashCode() & Integer.MAX_VALUE;
        CreditResponse cred = new CreditResponse(
                creditNo,
                accountNo,
                creditAmount,
                enumRequest.getPeriodInMonth(),
                12.0 / enumRequest.getPeriodInMonth() * 10.0,
                (creditAmount / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                creditAmount + (creditAmount / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                creditAmount + (creditAmount / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                (creditAmount + (creditAmount / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0)) / enumRequest.getPeriodInMonth(),
                LocalDate.now()
        );
//        boolean credits = restTemplate.getForObject("http://account:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());
      boolean credits = restTemplate.getForObject("http://localhost:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());
        if (!credits) {
            if (creditAmount <= 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not create credit.\n" +
                        "- Account (account number: " + accountNo + ") does not exist.\n" +
                        "- Credit amount should be over 0!");
            } else
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not create credit.\n" +
                        "- Account (account number: " + accountNo + ") does not exist.");
        } else if (credits) {
//            String accountType = restTemplate.getForObject("http://account:8085/api/accounts/" + accountNo + "/accountType", String.class);
            String accountType = restTemplate.getForObject("http://localhost:8085/api/accounts/" + accountNo + "/accountType", String.class);

            if (accountType.equals("Tagesgeldkonto")) {
                if (creditAmount > 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not create credit.\n" +
                            "- No credits allowed for account type: " + accountType);
                } else
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not create credit.\n" +
                            "- No credits allowd for account type: " + accountType + "\n" +
                            "- Credit amount should be over 0!");
            } else if (!accountType.equals("Tagesgeldkonto")){
                if (creditAmount > 0) {
                    creditService.requestCredit(cred);
                    return ResponseEntity.ok("Credit for account (" + accountNo + ") created.\n" +
                            "Credit amount: " + creditAmount + "\n" +
                            "Period in month: " + enumRequest.getPeriodInMonth() + "\n" +
                            "For more information, check credit with credit number: " + creditNo);
                } else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credit amount should be over 0!");
            }
        }
        return null;
    }

    @Operation(summary = "Pay off credit")
    @PutMapping("/credits/{creditNo}")
    public ResponseEntity<Object> updateCredit(
            @Parameter(description = "Credit number of credit to be paid off")
            @PathVariable Integer creditNo


    ) {
//        Optional<CreditResponse> credit = creditService.findByCreditNo(creditNo);
        if (creditService.findByCreditNo(creditNo).isPresent()) {
            CreditResponse credit = creditService.findByCreditNo(creditNo).orElseThrow();
            Double payedInterestRate = credit.getRemainingRepayment() - credit.getRates();
            if (payedInterestRate >= 0) {
                credit.setRemainingRepayment(payedInterestRate);
                return creditService.payCredit(credit);


            } else
                creditService.deleteByCreditNo(creditNo);
            return ResponseEntity.status(HttpStatus.OK).body("Credit is payed off");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit (credit number: " + creditNo + ") not found.");
    }

    @Hidden
    @Operation(summary = "Delete credit by credit number")
    @DeleteMapping("/credits/{creditNo}")
    public ResponseEntity deleteCredit(
            @Parameter(description = "Credit number of credit to delete")
            @PathVariable Integer creditNo
    ) {
        Optional<CreditResponse> credit = creditService.findByCreditNo(creditNo);

        if (credit.isPresent()) {
            creditService.deleteByCreditNo(creditNo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Credit (credit number: " + creditNo + ") deleted.");

        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not delete. Credit (credit number: " + creditNo + ") does not exist.");

    }

    @Hidden
    @DeleteMapping("/credits/account-credits/{accountNo}")
    public Void deleteCreditsOfAccount(
            @PathVariable Integer accountNo
    ) {
        List<CreditResponse> credit = creditService.findCreditByAccountNo(accountNo);

        if (credit.isEmpty()) {
            return null;
        } else {
            return creditService.deleteCreditByAccountNo(accountNo);
        }
    }
}
