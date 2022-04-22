package com.example.credit.service;

import com.example.credit.model.CreditResponse;
import com.example.credit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

//        boolean credits = restTemplate.getForObject("http://account:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());
      boolean credits = restTemplate.getForObject("http://localhost:8085/api/accounts/numbers", List.class).contains(cred.getAccountNo());


        if (!credits) {
            return null;
        } else {
//            restTemplate.put("http://account:8085/api/accounts/"+cred.getAccountNo()+"/deposit/"+cred.getCreditAmount(),List.class);
            restTemplate.put("http://localhost:8085/api/accounts/" + cred.getAccountNo() + "/deposit/" + cred.getCreditAmount(), List.class);
            return creditRepository.save(cred);


        }

    }

    public Optional<CreditResponse> findByCreditNo(Integer creditNo) {
        return creditRepository.findById(creditNo);
    }

    public void deleteByCreditNo(Integer creditNo) {
        creditRepository.deleteById(creditNo);
    }

    public CreditResponse payCredit(CreditResponse credit) {

        restTemplate.put("http://localhost:8085/api/accounts/"+credit.getAccountNo()+"/withdraw/"+credit.getRates(),List.class);
//        restTemplate.put("http://account:8085/api/accounts/" + credit.getAccountNo() + "/withdraw/" + credit.getInterest(), List.class);
        return creditRepository.save(credit);
    }
}
