package com.m3hho.cyberark.conjur.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SecretConjur {

  @Getter
  private String secretPath;

  @Getter
  private String secretValue;

}
