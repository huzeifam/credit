package com.example.credit.controller;


import com.example.credit.model.CreditCreateRequest;
import com.example.credit.model.CreditResponse;
import com.example.credit.model.CreditResponseEnum;
import com.example.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit with credit number " + creditNo + " does not exist");
    }

    @Operation(summary = "Request credit")
    @PostMapping("/credits")
    public CreditResponse requestCredit(
            @Parameter(description = "Account number of account to request credit")
            @RequestBody CreditCreateRequest cRequest,
            @Parameter(description = "Set period (in month) for credit")
            @RequestParam CreditResponseEnum enumRequest
    ) {
        CreditResponse cred = new CreditResponse(
                UUID.randomUUID().hashCode() & Integer.MAX_VALUE,
                cRequest.getAccountNo(),
                cRequest.getCreditAmount(),
                enumRequest.getPeriodInMonth(),
                12.0 / enumRequest.getPeriodInMonth() * 10.0,
                (cRequest.getCreditAmount() / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                cRequest.getCreditAmount() + (cRequest.getCreditAmount() / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                cRequest.getCreditAmount() + (cRequest.getCreditAmount() / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0),
                (cRequest.getCreditAmount() + (cRequest.getCreditAmount() / 100.0) * (12.0 / enumRequest.getPeriodInMonth() * 10.0))/enumRequest.getPeriodInMonth(),
                LocalDate.now()
        );
        return creditService.requestCredit(cred);
    }

    @Operation(summary = "Pay off credit")
    @PutMapping("/credits/{creditNo}")
    public CreditResponse updateCredit(
            @Parameter(description = "Credit number of credit to be paid off")
            @PathVariable Integer creditNo


    ) {
        CreditResponse credit = creditService.findByCreditNo(creditNo).orElseThrow();
        Double payedInterestRate = credit.getRemainingRepayment() - credit.getRates();
        if (payedInterestRate >= 0) {
            credit.setRemainingRepayment(payedInterestRate);
            CreditResponse savedCredit = creditService.payCredit(credit);
            return savedCredit;

        } else
            creditService.deleteByCreditNo(creditNo);
            return null;


    }


    @Operation(summary = "Delete credit by credit number")
    @DeleteMapping("/credits/{creditNo}")
    public ResponseEntity deleteCredit(
            @Parameter(description = "Credit number of credit to delete")
            @PathVariable Integer creditNo
    ) {
        Optional<CreditResponse> credit = creditService.findByCreditNo(creditNo);

        if (credit.isPresent()) {
            creditService.deleteByCreditNo(creditNo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Credit with credit number " + creditNo + " deleted.");

        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not delete. Credit with credit number " + creditNo + " does not exist.");

    }
}
