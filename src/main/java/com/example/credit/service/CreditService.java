package com.example.credit.service;

import com.example.credit.model.CreditResponse;
import com.example.credit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Autowired
    RestTemplate restTemplate;

    public List<CreditResponse> findAllCredits() {
        return creditRepository.findAll();
    }

    public CreditResponse requestCredit(CreditResponse cred) {

        boolean credits = restTemplate.getForObject("http://account:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());
//      boolean credits = restTemplate.getForObject("http://localhost:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());


        if (!credits) {
            return null;
        } else {
            restTemplate.put("http://account:8085/api/accounts/"+cred.getAccountNo()+"/deposit/"+cred.getCreditAmount(),List.class);
//            restTemplate.put("http://localhost:8085/api/accounts/" + cred.getAccountNo() + "/deposit/" + cred.getCreditAmount(), List.class);
            return creditRepository.save(cred);


        }

    }

    public Optional<CreditResponse> findByCreditNo(Integer creditNo) {
        return creditRepository.findById(creditNo);
    }

    public void deleteByCreditNo(Integer creditNo) {
        creditRepository.deleteById(creditNo);
    }

    public ResponseEntity<Object> payCredit(CreditResponse credit) {

//        Double balance = restTemplate.getForObject("http://localhost:8085/api/accounts/"+credit.getAccountNo()+"/balance", Double.class);
        Double balance = restTemplate.getForObject("http://account:8085/api/accounts/"+credit.getAccountNo()+"/balance", Double.class);
        if ( balance < credit.getRates()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pay off credit failed. \n\n" +
                    "Rate: "+ credit.getRates()+"€\n" +
                    "Current balance of account ("+credit.getAccountNo()+"): "+balance+"€");
        }
        else {
//            restTemplate.put("http://localhost:8085/api/accounts/"+credit.getAccountNo()+"/withdraw/"+credit.getRates(),List.class);
            restTemplate.put("http://account:8085/api/accounts/" + credit.getAccountNo() + "/withdraw/" + credit.getRates(), List.class);
            creditRepository.save(credit);
            return ResponseEntity.ok(findByCreditNo(credit.getCreditNo()));
        }
    }

    public List<CreditResponse> findCreditByAccountNo(Integer accountNo) {
        return creditRepository.findCreditByAccountNo(accountNo);
    }

    public Void deleteCreditByAccountNo(Integer accountNo) {

        creditRepository.deleteCreditByAccountNo(accountNo);
        return null;
    }
}
