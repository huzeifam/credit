package com.example.kredit.service;

import com.example.kredit.model.KreditResponse;
import com.example.kredit.repository.KreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class KreditService {

    private final KreditRepository kreditRepository;

    public KreditService(KreditRepository kreditRepository) {
        this.kreditRepository = kreditRepository;
    }

    @Autowired
    RestTemplate restTemplate;

    public List<KreditResponse> findAllKredits() {
        return kreditRepository.findAll();
    }

    public KreditResponse requestKredit(KreditResponse kred) {

        boolean kredits = restTemplate.getForObject("http://account:8085/api/accounts/numbers", List.class).contains(kred.getAccountNo());
//      boolean kredits = restTemplate.getForObject("http://localhost:8085/api/accounts/numbers", List.class).contains(kred.getAccountNo());


        if (!kredits) {
            return null;
        } else {
            restTemplate.put("http://account:8085/api/accounts/"+kred.getAccountNo()+"/deposit/"+kred.getKreditAmount(),List.class);
//          restTemplate.put("http://localhost:8085/api/accounts/"+kred.getAccountNo()+"/deposit/"+kred.getKreditAmount(),List.class);
            return kreditRepository.save(kred);

        }

    }

    public Optional<KreditResponse> findByKreditNo(Integer kreditNo) {
        return kreditRepository.findById(kreditNo);
    }

    public void deleteByKreditNo(Integer kreditNo) {
        kreditRepository.deleteById(kreditNo);
    }

    public KreditResponse payKredit(KreditResponse kredit) {

//        restTemplate.put("http://localhost:8085/api/accounts/"+kredit.getAccountNo()+"/withdraw/"+kredit.getKreditAmount(),List.class);
        restTemplate.put("http://account:8085/api/accounts/"+kredit.getAccountNo()+"/withdraw/"+kredit.getInterestRate(),List.class);
        return kreditRepository.save(kredit);
    }
}
