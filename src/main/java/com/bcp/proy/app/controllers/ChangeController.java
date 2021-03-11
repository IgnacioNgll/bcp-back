package com.bcp.proy.app.controllers;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcp.proy.app.domain.Change;
import com.bcp.proy.app.dto.ResponseExchangeDto;
import com.bcp.proy.app.services.ChangeService;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/change")
public class ChangeController {

    private ChangeService changeService;
    
    public ChangeController(ChangeService changeService) {
        this.changeService = changeService;
    }


    @GetMapping("/list")
    public ResponseEntity<?> all() {
        HttpStatus status = HttpStatus.OK;
        List<Change> LitsChange = changeService.findAll().stream()
                .collect(Collectors.toList());
        return new ResponseEntity<>(LitsChange, status);
    }


    @GetMapping("/exchange/{amount}/{origin}/{destination}")
    public Single<ResponseEntity<ResponseExchangeDto>> getExchangeRate(
    		
    		@PathVariable String amount,
    		@PathVariable String origin,
    		@PathVariable String destination) {

        return changeService.exchangeAmount(amount,origin,destination)
                .subscribeOn(Schedulers.io())
                .map(result -> ResponseEntity.ok(result));

    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody Change body, @PathVariable Long id) {
    	changeService.findById(id)
                .map(result -> {
                	result.setValue(body.getValue());
                    return changeService.save(result);
                });
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
