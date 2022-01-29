package br.com.zaratech.security;

import static br.com.zaratech.util.GrantControlConstants.ALGORITHM_VALUE;
import static br.com.zaratech.util.GrantControlConstants.GERAR_HASH_VALUE;
import static br.com.zaratech.util.GrantControlConstants.UTF8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/* SpellCheckingInspection */
public class Scrypt {

    private static SecretKey skey;
    private static KeySpec ks;
    private static PBEParameterSpec ps;

    private Scrypt() {
    }

    static {
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_VALUE);
            ps = new PBEParameterSpec(new byte[] { 3, 1, 4, 1, 5, 9, 2, 6 }, 20);
            ks = new PBEKeySpec("1".toCharArray());
            skey = skf.generateSecret(ks);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());
        }
    }

    public static final String encrypt(final String text)
            throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnsupportedEncodingException {

        final Cipher cipher = Cipher.getInstance(ALGORITHM_VALUE);
        cipher.init(Cipher.ENCRYPT_MODE, skey, ps);
        String criptografado = new String(Base64.encodeBase64(cipher.doFinal(text.getBytes())), StandardCharsets.UTF_8);
        log.debug("Cripto Original: " + criptografado);
        criptografado = criptografado.replace("/", "{");
        log.debug("Cripto Substitu: " + criptografado);
        return URLEncoder.encode(criptografado, UTF8);
    }

    public static final String encryptHash(final String text)
            throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException,
            InvalidAlgorithmParameterException, UnsupportedEncodingException {
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_VALUE);
            PBEParameterSpec ps1 = new PBEParameterSpec(new byte[] { 3, 1, 4, 1, 5, 9, 2, 6 }, 20);

            String hash = gerarHash();

            KeySpec ks1 = new PBEKeySpec(hash.toCharArray());
            SecretKey skey1 = skf.generateSecret(ks1);

            final Cipher cipher = Cipher.getInstance(ALGORITHM_VALUE);
            cipher.init(Cipher.ENCRYPT_MODE, skey1, ps1);
            String criptografado = new String(Base64.encodeBase64(cipher.doFinal(text.getBytes())),
                    StandardCharsets.UTF_8);
            criptografado = criptografado.replace("/", "{");
            String senhaCripto = java.net.URLEncoder.encode(criptografado, UTF8);
            log.info("Encrypt: " + text + " Hash (" + hash + ") Cripyo " + criptografado);
            return hash.concat(senhaCripto);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());
        }
        return text;
    }

    private static String gerarHash() {

        String letras = GERAR_HASH_VALUE;
        StringBuilder armazenaChaves = new StringBuilder();
        int index = -1;
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 3; i++) {
            index = random.nextInt(letras.length());
            armazenaChaves.append(letras.substring(index, index + 1));
        }
        return armazenaChaves.toString();
    }

    public static final String decryptHash(String text)
            throws NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, UnsupportedEncodingException {

        text = URLDecoder.decode(text, UTF8);
        text = text.replace("{", "/");
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_VALUE);
            PBEParameterSpec ps1 = new PBEParameterSpec(new byte[] { 3, 1, 4, 1, 5, 9, 2, 6 }, 20);

            String hash = text.substring(0, 3);
            text = text.substring(3, text.length());

            KeySpec ks1 = new PBEKeySpec(hash.toCharArray());
            SecretKey skey1 = skf.generateSecret(ks1);

            final Cipher cipher = Cipher.getInstance(ALGORITHM_VALUE);
            cipher.init(Cipher.DECRYPT_MODE, skey1, ps1);
            String ret = null;

            ret = new String(cipher.doFinal(Base64.decodeBase64(text.getBytes())), StandardCharsets.UTF_8);

            log.info("Encrypt: " + ret + " Hash (" + hash + ") Cripto " + text);
            return ret;
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException
                | BadPaddingException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    public static final String decrypt(String text)
            throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnsupportedEncodingException {

        text = java.net.URLDecoder.decode(text, UTF8);
        log.debug("Cripto Original: " + text);
        text = text.replace("{", "/");
        text = text.replace(" ", "+");
        log.debug("Cripto Substitu: " + text);

        final Cipher cipher = Cipher.getInstance(ALGORITHM_VALUE);
        cipher.init(Cipher.DECRYPT_MODE, skey, ps);
        String ret = null;
        ret = new String(cipher.doFinal(Base64.decodeBase64(text.getBytes())), StandardCharsets.UTF_8);
        return ret;
    }
}