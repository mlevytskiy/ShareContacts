package sharecontacts.com.sharecontacts.util;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sharecontacts.com.sharecontacts.BuildConfig;

/**
 * Created by max on 27.03.17.
 */

public class SecureUtils {

    public static final String ALGORITHM = "AES";
    public static final String ALGORITHM_FLAVOUR = "AES/CBC/PKCS5Padding";

    public static String decrypt(String base64) {
        byte[] fileBytes = Base64.decode(base64, Base64.DEFAULT);
        return decrypt(fileBytes);
    }

    public static String decrypt(byte[] bytes) {
        try {
            Cipher cipher = buildCipher(Cipher.DECRYPT_MODE);
            byte[] decrypted = cipher.doFinal(bytes);
            return new String(decrypted);
        } catch (Exception e) {
            Log.e("testr", "problem with bytes decryption", e);
        }
        return null;
    }

    public static byte[] encrypt(byte[] bytes) {
        try {
            Cipher cipher = buildCipher(Cipher.ENCRYPT_MODE);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            Log.e("testr", "problem with bytes encryption", e);
        }
        return null;
    }

    private static Cipher buildCipher(int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] key = obfuscatedKey();
        if (BuildConfig.DEBUG) {
            Log.d("testr", "aes cbc key=" + byteArrayToHex(key));
        }
        SecretKeySpec sks = new SecretKeySpec(key, ALGORITHM);
        byte[] ivBytes = obfuscatedIV();
        if (BuildConfig.DEBUG) {
            Log.d("testr", "aes cbc iv=" + byteArrayToHex(ivBytes));
        }
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(ALGORITHM_FLAVOUR);
        cipher.init(cipherMode, sks, iv);
        return cipher;
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] obfuscatedKey() {
        byte[] byteArray1 = hexStringToByteArray("EFAA947F057BB565A991BE6735449CDB");
        byte[] byteArray2 = hexStringToByteArray("957559FD8A6F56B7D1FB9E31CCB2E52C");
        byte[] result = new byte[byteArray1.length];
        for (int i = 0; i < byteArray1.length; i++) {
            result[i] = (byte) (byteArray1[i] ^ byteArray2[i]);
        }
        return result;
    }

    private static byte[] obfuscatedIV() {
        byte[] byteArray1 = hexStringToByteArray("285F02ED34B3871A70752592776E9244");
        byte[] byteArray2 = hexStringToByteArray("3F97BF60985D3C1B7D1CD6FF5586D44F");
        byte[] result = new byte[byteArray1.length];
        for (int i = 0; i < byteArray1.length; i++) {
            result[i] = (byte) (byteArray1[i] ^ byteArray2[i]);
        }
        return result;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2) {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
