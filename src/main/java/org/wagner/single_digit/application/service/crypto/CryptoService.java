package org.wagner.single_digit.application.service.crypto;

import org.springframework.stereotype.Service;
import org.wagner.single_digit.presentation.exception.CryptoException;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {
    private static final String ALGORITHM = "RSA";

    public String encrypt(String plainText, String publicKey) {
        try {
            PublicKey key = decodePublicKey(publicKey);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new CryptoException("Failed to encrypt data", e);
        }
    }

    public PublicKey decodePublicKey(String publicKey)  {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new CryptoException("Invalid public key", e);
        }
    }
}
