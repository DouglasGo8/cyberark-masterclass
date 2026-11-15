package com.m3hho.cyberark.conjur;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CyberArkConjurApp {
  static void main(String[] args) {
    log.info("CyberArkConjurApp starts....");
    SpringApplication.run(CyberArkConjurApp.class, args);
  }
}
