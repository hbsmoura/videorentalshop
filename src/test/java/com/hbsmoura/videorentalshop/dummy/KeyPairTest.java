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
import java.util.Base64;

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

        String publicKeyPathString = "src/main/resources/certs/public.pem";
        String privateKeyPathString = "src/main/resources/certs/private.pem";

        // Create the key files on the given path
        // To be run only once or these will override the files everytime
        //*******************************************
//        String publicKeyPem =
//                "-----BEGIN RSA PUBLIC KEY-----\n"
//                + Base64.getEncoder().encodeToString(publicKey.getEncoded())
//                + "\n-----END RSA PUBLIC KEY-----";
//
//        FileOutputStream filePublicKeyOutput = new FileOutputStream(publicKeyPathString);
//        filePublicKeyOutput.write(publicKeyPem.getBytes());
//        filePublicKeyOutput.close();
//
//        String privateKeyPem =
//                "-----BEGIN RSA PRIVATE KEY-----\n"
//                + Base64.getEncoder().encodeToString(privateKey.getEncoded())
//                + "\n-----END RSA PRIVATE KEY-----";
//
//        FileOutputStream filePrivateKeyOutput = new FileOutputStream(privateKeyPathString);
//        filePrivateKeyOutput.write(privateKeyPem.getBytes());
//        filePrivateKeyOutput.close();
        //*******************************************

        // Retrieve extract the keys from the files
        Path publicPemPath = Paths.get(publicKeyPathString);
        String publicPem = new String(Files.readAllBytes(publicPemPath));
        String base64ExtractedPublicKey = publicPem.replace("-----BEGIN RSA PUBLIC KEY-----\n", "");
        base64ExtractedPublicKey = base64ExtractedPublicKey.replace("\n-----END RSA PUBLIC KEY-----", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(base64ExtractedPublicKey);

        Path privatePemPath = Paths.get(privateKeyPathString);
        String privatePem = new String(Files.readAllBytes(privatePemPath));
        String base64ExtractPrivateKey = privatePem.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
        base64ExtractPrivateKey = base64ExtractPrivateKey.replace("\n-----END RSA PRIVATE KEY-----", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(base64ExtractPrivateKey);

        // For check out the keys format
        // That's how we retrieve keys from strings
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey returnedPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        PrivateKey returnedPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

    }
}
