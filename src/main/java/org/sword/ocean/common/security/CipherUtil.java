/**
 * 
 */
package org.sword.ocean.common.security;

import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author ChengNing
 * @date 2015年11月25日
 */
public class CipherUtil {

	public static String DESDecrypt(String text, String key) {
		byte[] bytesrc = convertHexString(text);
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] retByte = cipher.doFinal(bytesrc);
			return new String(retByte);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
//		return null;
	}

	private static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	public static String DESEncrypt(String value, String key) {
		String result = "";
		try {
			value = java.net.URLEncoder.encode(value, "utf-8");
			result = toHexString(encrypt(value, key)).toUpperCase();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		return result;
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2) plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	public static void main(String[] args) throws Exception {
		long time = 1448420802219l;
		Date now = new Date(time);
		String value = "testtest";
		String key = "12345678"; 
		value =  String.valueOf(now.getTime());
		//6ED959688AEC719B15C1B4DF4AC33905
		//6ED959688AEC719B15C1B4DF4AC33905
		System.out.println("加密数据:" + value);
		System.out.println("密码为:" + key);
		String a = DESEncrypt(value, key);
		System.out.println("加密后的数据为:" + a);
	}
}
