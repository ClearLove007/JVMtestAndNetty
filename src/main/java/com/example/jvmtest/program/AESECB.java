package com.example.jvmtest.program;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 朱元浩
 * @Description: AES (ECB模式) 对称密钥加密
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。
 * 这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB 197，
 * 并在2002年5月26日成为有效的标准。2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。
 * @date 2018年12月04日
 */
public class AESECB {
    private static final String CHARSET = "UTF-8";

    public static String encrypt(String text, String key) {
        try {
            return encrypt(text, key, CHARSET);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String decrypt(String text, String key) throws Exception{
        try {
            return decrypt(text, key, CHARSET);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String encrypt(String text, String key, String charset) {
        try {
            if (key == null) {
                throw new RuntimeException("key is null");
            } else if (16 != key.length() && 24 != key.length() && 32 != key.length()) {
                throw new RuntimeException("key must be 16/24/32");
            } else {
                byte[] raw = key.getBytes(charset);
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(1, skeySpec);
                byte[] encrypted = cipher.doFinal(text.getBytes(charset));
                return parseByte2HexStr(encrypted);
            }
        } catch (Exception var7) {
            throw new RuntimeException(var7);
        }
    }

    public static String decrypt(String text, String key, String charset) throws Exception {
        try {
            if (key == null) {
                throw new RuntimeException("key is null");
            } else if (16 != key.length() && 24 != key.length() && 32 != key.length()) {
                throw new RuntimeException("key must be 16/24/32");
            } else {
                byte[] raw = key.getBytes(charset);
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(2, skeySpec);
                byte[] encrypted1 = parseHexStr2Byte(text);

                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original, charset);
                    return originalString;
                } catch (Exception var9) {
                    throw new RuntimeException(var9);
                }
            }
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        }
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toLowerCase());
        }

        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for(int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte)(high * 16 + low);
            }

            return result;
        }
    }

    public static void main(String[] args){
        String enOrderNo = AESECB.encrypt("1060488462247538688", "1A2B3C4D5F6G7H8I");
        System.out.println(enOrderNo);
    }
}
