package user;

import java.security.Key;

import javax.crypto.Cipher;

public class MakeToken {

	public final static String KEY = "ItisImpor";

	private static Cipher mEncryptCipher = null;
	private static Cipher mDecryptCipher = null;

	public MakeToken() throws Exception {
		mEncryptCipher = Cipher.getInstance("DES");
		mEncryptCipher.init(Cipher.ENCRYPT_MODE, getKey(KEY.getBytes()));
		mDecryptCipher = Cipher.getInstance("DES");
		mDecryptCipher.init(Cipher.DECRYPT_MODE, getKey(KEY.getBytes()));
	}

	// encode
	public String encrypt(String strIn) throws Exception {
		return byte2HexStr(encrypt(strIn.getBytes()));
	}

	public byte[] encrypt(byte[] arrB) throws Exception {
		return mEncryptCipher.doFinal(arrB);
	}

	// decode
	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2Byte(strIn)));
	}

	public byte[] decrypt(byte[] arrB) throws Exception {
		return mDecryptCipher.doFinal(arrB);
	}

	private Key getKey(byte[] arrBTmp) throws Exception {
		byte[] arrB = new byte[8];

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	public static byte[] hexStr2Byte(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public static String byte2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		try {

			System.out.println("加密前：");
			MakeToken des = new MakeToken();
			String password = des.encrypt("suara1201fxt@gmail.com");
			System.out.println("加密后：" + password);
			password = des.decrypt(password);
			System.out.println("解密密后：" + password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}