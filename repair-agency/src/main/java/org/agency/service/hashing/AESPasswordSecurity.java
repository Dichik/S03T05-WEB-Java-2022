package org.agency.service.hashing;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESPasswordSecurity implements PasswordEncryptor, PasswordDecryptor {

    private SecretKey key;
    private IvParameterSpec ivParameterSpec;

    public AESPasswordSecurity() {
//        this.key = AESUtil.generateKey(128);
//        this.ivParameterSpec = AESUtil.generateIv();
    }

    @Override
    public String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    @Override
    public String decrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(input));
        return new String(plainText);
    }

}
