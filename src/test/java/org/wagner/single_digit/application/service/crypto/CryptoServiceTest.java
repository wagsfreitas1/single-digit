package org.wagner.single_digit.application.service.crypto;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.wagner.single_digit.presentation.exception.CryptoException;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CryptoServiceTest {

    private final CryptoService cryptoService = new CryptoService();

    @Test
    public void encrypt_givenTextAndKey_shouldReturnEncryptedText() throws Exception {
        String validKeyBase64 = getValidKeyBase64();

        String plainText = "test";
        assertNotNull(validKeyBase64, "Public key not found in test.properties");

        String result = cryptoService.encrypt(plainText, validKeyBase64);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertNotEquals(plainText, result);
    }

    @Test
    public void encrypt_givenTextAndWrongKey_shouldThrowCryptoException() {
        String plainText = "test";
        String validKeyBase64 = "wrongKey";
        assertNotNull(validKeyBase64, "Public key not found in test.properties");

        CryptoException exception = assertThrows(CryptoException.class, () -> cryptoService.encrypt(plainText, validKeyBase64));

        assertEquals("Failed to encrypt data", exception.getMessage());
    }

    @Test
    public void decodePublicKey_givenValidKeyString_shouldReturnPublicKey() throws IOException {
        String validKey = getValidKeyBase64();

        PublicKey result = cryptoService.decodePublicKey(validKey);

        assertNotNull(result);
        assertEquals("RSA", result.getAlgorithm());
        assertEquals("X.509", result.getFormat());
        assertNotNull(result.getEncoded());
        assertTrue(result.getEncoded().length > 0);
    }

    private String getValidKeyBase64() throws IOException {
        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.properties");
        assertNotNull(inputStream, "test.properties file not found");
        props.load(inputStream);
        return props.getProperty("test.rsa.public.key");
    }
}