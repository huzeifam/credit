package com.example.kredit.controller;


import com.example.kredit.model.KreditCreateRequest;
import com.example.kredit.model.KreditResponse;
import com.example.kredit.service.KreditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class KreditController {

    private final KreditService kreditService;

    public KreditController(KreditService kreditService) {
        this.kreditService = kreditService;
    }

    @GetMapping("/kredits")
    public List<KreditResponse> getAllKredits() {
        return kreditService.findAllKredits();
    }

    @GetMapping("/kredits/{kreditNo}")
    public ResponseEntity<Object> getKreditByKreditNo(
            @PathVariable Integer kreditNo
    ) {
        Optional<KreditResponse> kredit = kreditService.findByKreditNo(kreditNo);
        if (kredit.isPresent())
            return ResponseEntity.ok(kredit.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kredit with kredit number " + kreditNo + " does not exist");
    }

    @PostMapping("/kredits")
    public KreditResponse requestKredit(
            @RequestBody KreditCreateRequest kRequest
    ) {
        KreditResponse kred = new KreditResponse(
                UUID.randomUUID().hashCode() & Integer.MAX_VALUE,
                kRequest.getAccountNo(),
                kRequest.getKreditAmount(),
                kRequest.getInterestRatePercentage(),
                (kRequest.getKreditAmount() / 100) * kRequest.getInterestRatePercentage(),
                LocalDateTime.now()
        );
        return kreditService.requestKredit(kred);
    }


    @PutMapping("/kredits/{kreditNo}")
    public KreditResponse updateKredit(
            @PathVariable Integer kreditNo


    ) {
        KreditResponse kredit = kreditService.findByKreditNo(kreditNo).orElseThrow();
        Double payedInterestRate = kredit.getKreditAmount() - kredit.getInterestRate();
        kredit.setKreditAmount(payedInterestRate);

        KreditResponse savedKredit = kreditService.payKredit(kredit);
        return savedKredit;
    }



    @DeleteMapping("/kredits/{kreditNo}")
    public ResponseEntity deleteKredit(
            @PathVariable Integer kreditNo
    ) {
        Optional<KreditResponse> kredit = kreditService.findByKreditNo(kreditNo);

        if (kredit.isPresent()) {
            kreditService.deleteByKreditNo(kreditNo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Kredit with kredit number " + kreditNo + " deleted.");

        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not delete. Kredit with kredit number " + kreditNo + " does not exist.");

    }
}
