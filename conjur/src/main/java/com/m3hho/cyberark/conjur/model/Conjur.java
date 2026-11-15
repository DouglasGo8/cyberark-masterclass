package com.m3hho.cyberark.conjur.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

@NoArgsConstructor
@AllArgsConstructor
public class Conjur {

  @Getter
  private String body;

  @Handler
  public Conjur myConjur(@Body String body) {
    return new Conjur("I got your secret: ".concat(body));
  }
}
