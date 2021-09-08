package com.PropTech.UserRegist.PropTechApp.PropTechAPI.PasswordHashUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHashUtil {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATION_COUNT = 10000;
	private static final int KEY_LENGTH = 256;
	
	public static String getSaftyPassword(String password, String salt) {
		char[] passCharAry = password.toCharArray();
		byte[] hashedSalt = getHashedSalt(salt);
		
		PBEKeySpec keySpec = new PBEKeySpec(passCharAry, hashedSalt, ITERATION_COUNT, KEY_LENGTH);
		SecretKeyFactory skf;
		
		try {
			skf = SecretKeyFactory.getInstance(ALGORITHM);
		}catch(NoSuchAlgorithmException exception) {
			throw new RuntimeException(exception);
		}
		
		SecretKey secretKey;
		try {
			secretKey = skf.generateSecret(keySpec);
		}catch(InvalidKeySpecException exception) {
			throw new RuntimeException(exception);
		}
		byte[] passByteAry = secretKey.getEncoded();
		
		StringBuilder base64 = new StringBuilder(64);
		for(byte Byte : passByteAry) {
			base64.append(String.format("%02x", Byte & 0xff));
		}
		return base64.toString();
	}
	
	private static byte[] getHashedSalt(String salt) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException exception) {
			throw new RuntimeException(exception);
		}
		messageDigest.update(salt.getBytes());
		return messageDigest.digest();
	}
}
