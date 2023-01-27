package com.hbsmoura.videorentalshop.dummy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KeyPairTest {

    @Test
    void generateKeyPairTest() throws NoSuchAlgorithmException, InvalidKeySpecException {

        // A new keypair with RSA Algorithm and of size 2048
        // To be run only once in these case
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.genKeyPair();

        // Base64 String of the public and the private keys
        // To be stored on a file like application.properties
        String publicKeyValue = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyValue = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        // For check out the keys format
        // That's how we retrieve keys from strings
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyValue)));
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyValue)));

        // Printing this on terminal you can just copy and paste the generated keys
        System.out.println();
        System.out.println("***** Public Key *****");
        System.out.println(publicKeyValue);
        System.out.println("*********************************");
        System.out.println();
        System.out.println("***** Private Key *****");
        System.out.println(privateKeyValue);
        System.out.println("*********************************");
        System.out.println("");

    }
}
