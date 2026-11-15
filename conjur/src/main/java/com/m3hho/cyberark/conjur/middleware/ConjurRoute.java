package com.m3hho.cyberark.conjur.middleware;

import com.m3hho.cyberark.conjur.model.Conjur;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ConjurRoute extends RouteBuilder {
  @Override
  public void configure() {

    from("direct://conjur-get").routeId("conjur-get")
            .log("Got secret: ${body}")
            .setHeader("CamelCyberArkVaultSecretId", simple("poc/app/${body}"))
            .setHeader("CamelCyberArkVaultOperation", constant("getSecret"))
            .to("cyberark-vault:://")
            .bean(Conjur.class)
            .end();


    from("seda://conjur-save").routeId("conjur-save")
            .log("Saving your secret: ${body.secretPath}")
            .setHeader("CamelCyberArkVaultSecretId", simple("poc/app/${body.secretPath}"))
            .setHeader("CamelCyberArkVaultOperation", constant("createSecret"))
            .setBody(simple("${body.secretValue}"))
            .to("cyberark-vault:://")
            .log("Secret created/updated at: ${header.CamelCyberArkVaultSecretId}")
            .end();

  }
}
