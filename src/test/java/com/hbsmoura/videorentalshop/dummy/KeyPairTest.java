package com.hbsmoura.videorentalshop.dummy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KeyPairTest {

    @Test
    void generateKeyPairTest() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        // A new keypair with RSA Algorithm and of size 2048
        // To be run only once in these case
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.genKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyPathString = "src/main/resources/certs/public.der";
        String privateKeyPathString = "src/main/resources/certs/private.der";

        // Create the key files on the given path
        // To be run only once or these will override the files everytime
        //*******************************************
//        FileOutputStream filePublicKeyOutput = new FileOutputStream(publicKeyPathString);
//        filePublicKeyOutput.write(publicKey.getEncoded());
//        filePublicKeyOutput.close();
//
//        FileOutputStream filePrivateKeyOutput = new FileOutputStream(privateKeyPathString);
//        filePrivateKeyOutput.write(privateKey.getEncoded());
//        filePrivateKeyOutput.close();
        //*******************************************

        // Retrieve the keys from the files
        Path publicKeyPath = Paths.get(publicKeyPathString);
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyPath);

        Path privateKeyPath = Paths.get(privateKeyPathString);
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyPath);

        // For check out the keys format
        // That's how we retrieve keys from strings
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey returnedPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        PrivateKey returnedPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

    }
}
