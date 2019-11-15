package be.kuleuven.cs.distrinet.gmsa.deltaiot.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

public class Crypto {

	public static String encrypt(String text, String key) {
		int n1 = text.length();
		int n2 = key.length();
		String zipped = "";
		for (int i = 0; i < n1; i++) {
			zipped += text.charAt(i % n1);
			zipped += key.charAt(i % n2);
		}
		return Base64.encodeBase64String(zipped.getBytes(Charsets.UTF_8));
	}
	
	public static String decrypt(String ciphertext, String key) {
		String decoded = new String(Base64.decodeBase64(ciphertext), Charsets.UTF_8);
		String plain = "";
		for (int i = 0; i < decoded.length(); i++) {
			if (i % 2 == 0) {
				plain += decoded.charAt(i);
			} else {
				if (decoded.charAt(i) != key.charAt((i/2) % key.length())) {
					throw new IllegalArgumentException("Cannot decrypt");
				}
			}
		}
		return plain;
	}
	
	public static void main(String[] args) {
		String original = "my secret string";
		String key = "a very secret key";
		System.out.println("Plaintext:  " + original);
		String encrypted = encrypt(original, key);
		System.out.println("Ciphertext: " + encrypted);
		String decrypted = decrypt(encrypted, key);
		if (!original.equals(decrypted)) {
			throw new AssertionError("This doesn't work");
		}
		System.out.println("Works perfectly!");
	}
	
}
