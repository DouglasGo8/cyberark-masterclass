package com.m3hho.cyberark.conjur.api;

import com.m3hho.cyberark.conjur.model.Conjur;
import com.m3hho.cyberark.conjur.model.SecretConjur;
import lombok.AllArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@AllArgsConstructor
@RequestMapping("/conjur/api/")
public class ConjurController {

  @Autowired
  final ProducerTemplate service;

  @GetMapping("/my-secret/{secret}")
  public ResponseEntity<Conjur> getSecret(@PathVariable String secret) {
    return ResponseEntity.ok(service.requestBody("direct:conjur-get", secret, Conjur.class));
  }

  @PostMapping("/my-secret/save")
  public ResponseEntity<SecretConjur> saveSecret(@RequestBody SecretConjur secretConjur) {

    service.asyncRequestBody("seda:conjur-save", secretConjur);

    return ResponseEntity.created(ServletUriComponentsBuilder
            .fromCurrentRequest()

            .buildAndExpand(secretConjur.getSecretPath())
            .toUri()).body(secretConjur);
  }
}
