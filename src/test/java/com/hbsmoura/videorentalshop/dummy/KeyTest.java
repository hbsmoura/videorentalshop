package com.hbsmoura.videorentalshop.dummy;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.util.Base64;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KeyTest {

    @Test
    void generateKeyTest() {

        // A new secret nad shared key
        // To be run only once
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // A Base64 encoded string for the key to be stored
        String keyValue = Base64.getEncoder().encodeToString(key.getEncoded());

        // We can print out the key for copying and paste
        System.out.println();
        System.out.println("********************************");
        System.out.println("***** Secret Key");
        System.out.println(keyValue);
        System.out.println("********************************");
        System.out.println();
    }
}
