package poneytoponey;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.NoSuchPaddingException;

import crypto.Crypto;
import crypto.RSA;

// Some basic hand-crafted tests without JUnit to verify correctness of implementation
public class RSACryptoCheck {
	public class PemUtils {

		public static PublicKey readPublicKey(String pem) throws Exception {
			String cleaned = pem.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
					.replaceAll("\\s", "");
			byte[] decoded = Base64.getDecoder().decode(cleaned);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePublic(spec);
		}

		public static PrivateKey readPrivateKey(String pem) throws Exception {

			String cleaned = pem.replace("-----BEGIN RSA PRIVATE KEY-----", "")
					.replace("-----END RSA PRIVATE KEY-----", "").replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
			byte[] decoded = Base64.getDecoder().decode(cleaned);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePrivate(spec);
		}
	}

	public static void main(String[] args) throws Exception, NoSuchAlgorithmException {
		System.out.println("CRYPTO MANUAL TEST");

		Crypto crypto;
		try {
			crypto = new RSA();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("Stop");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException("Stop");
		}

		// Keypair generated with the help of
		// https://www.devglan.com/online-tools/rsa-encryption-decryption
		PublicKey publicKey = PemUtils.readPublicKey(
				"-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA+ZkGBXBkakYK8JTH0CAw9bBh4AEHgdbNA6sHERtDrftBdoBTAZ7fMd4T7LKSRf6L1aabJyGM0gYrsIAkBd09hkVaWWHoUDMv/YS0PE/JPVSBORppynU0JaQeK/2aviMoN6ui+5JzLmHoiVbC1Z/pVFRJXGOBaNn21rEvanIAnQqTBSaeHg2J8GjDsWl1sLwc+CWzkiblLdSWbvSFkfLkhtfWhZ9a4an+6UdDyoldfXrT21UhbhF535+YKw8nMmIJX1+5G/e9RQDjxhQYwyjFu0fodZ/7UKWLSm7tqwk4r/W5co/g11xwAohSdGxNFvyw6NelklsdznoWJ1rQIXumIQIDAQAB\n-----END PUBLIC KEY-----");
		PrivateKey privateKey = PemUtils.readPrivateKey(
				"-----BEGIN RSA PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQD5mQYFcGRqRgrwlMfQIDD1sGHgAQeB1s0DqwcRG0Ot+0F2gFMBnt8x3hPsspJF/ovVppsnIYzSBiuwgCQF3T2GRVpZYehQMy/9hLQ8T8k9VIE5GmnKdTQlpB4r/Zq+Iyg3q6L7knMuYeiJVsLVn+lUVElcY4Fo2fbWsS9qcgCdCpMFJp4eDYnwaMOxaXWwvBz4JbOSJuUt1JZu9IWR8uSG19aFn1rhqf7pR0PKiV19etPbVSFuEXnfn5grDycyYglfX7kb971FAOPGFBjDKMW7R+h1n/tQpYtKbu2rCTiv9blyj+DXXHACiFJ0bE0W/LDo16WSWx3OehYnWtAhe6YhAgMBAAECggEAGR+NnzpIBzjv5LWrE6BL4U0M2v/rhiaNegUq585FXpOr7A5AXKlFyMaUXZa/Wa+Emn+jvhl2y9SebOyHA7aYWOWt8ZGE8V6nodZr0GbNCCDzhWCK3bu0oSXO+ilTnDgcdhcPB5C/CoAFKJ7Mq3VGRoxZ2oRzDA9ldMh+r/GT7XqGPOrz6ZKovl+7J8+qP7U8QCtTC1z4RburNHukaYs+KWysCi7i6EKgNE5HssFXwmflu1BTJ+6UorWCMhQNr+KVwoTkfhfgFNtoB0AkBN9nyX0yYUX3az1P98YsAjhvhNbd/ILbywv3daXIhnCh8Y9YB0L+11hJxtHmWOBafGRzOQKBgQD81LmzGU9fmUiuyVrZ8yeqlz04luRGCxkH8ACQZ9IhkcZjzp3Kv8uKn1TK0KI1WaB2mFn+9iAFKfso9kGk/OgkHLE4kmfXXALq73W5C+sJm/RHVylFmGoLcrXqw/4tBT7n7RYLu+Yjtnmfkykn6WRL7nOm8VdCjMH44uXaGogcyQKBgQD8uexsLKsZOFFcif59ukqhmf8IsLhB0wdZux2fMENmL5AMy/6HFF4QrA2+mcjKCAgOGGvZFnQv0cb1I1beMzvxbq8Gin0pZvyWzw3lll0+0Pq3XKFDxuv/ohXCscgeAePxrGf0XGHXXXBO2qOy0Z6Zqkkivatnc0UScaLGKHHimQKBgQDJIs6MzcDYc9BfxK4zH37Z9rFsmJYHpwKFzwgT2ZRQwnDKq+/aARUDNdg19R5mROQkFPXboKA+m3RE1QvKHqzrNaxDLi3QJVHo5xGHYhTcsyX1r0CVLjiG3P4eQ0etNzQhO/rC4PuoQ52kNmWSa9kvkwaMtAjQV7xkrNVg2qSYCQKBgQDw30e95mkA9ZNeTsu3C1pcYckYgE5ttnN4C4Z1Fev3NffmqnsiRcBNxZ6n/izImhULUi1gUC1W8mrdc9w0lcJZHzREWLRoRJcUKa5GFyUyXL3JC8lMLwj5sYNp+EYSi5y016yQ04gRWyZSkcKaCX5PWWSxbvMx+0RnCtWoZh39aQKBgQCEVW9LSSUcb8xmWA00aB+g/kXbiXPoyjA1QcsDOfh8Dq83BLzJhpAgcIQPynN7BkPIPpLj+EAXDcgFCque1ee6gNBzYsojeBrySYQZqlBy5uXmODD7f3meyVsVSXXBPZfkF9JeVFOKqTeDqbkuOp6htUGuFAz5HM1GZJh6ZYna+A==\n-----END RSA PRIVATE KEY-----");

		System.out.println("[OK] Keypair generated");

		// SIGNATURE TESTS

		// This was generated with the help of
		// https://www.lddgo.net/en/encrypt/rsa-sign-verify
		String message = "super important message to sign !!!!";
		String validSignatureBase64 = "fV5HGyIBJDCdp9NqfGZWiCdfa9axa+ZFoN9Zd2yOjTdhMrDRSMjkfH2zJRHR44QB2ClV2nAswPWmRsWpnLQgYV2jkHuLI/T/L7X/hhK7fo7yxlBcbCKcLr6jq/JL+vGl1L6eZ6tL2QdD77oXh6cnf0fc0838cvwxmQj9ojBuVZJCwrxTAp5IgP3yr7I3xY0J2INa2iAX5Wx+jLt+I4vYfaPZXFaIV0p/IQB8Xt9iXldByeLYNMXtscCSy4XzDRwKsnxV4IZaE+pJeVBFJWuvFl0nYwCwYZF7B1vP0AOXPMkduF2r7UVvkxPY2D647dXm/zTyFRmrOPX9v9azzQZzPg==";

		byte[] content = message.getBytes(StandardCharsets.UTF_8);

		System.out.println("SIGNATURE TESTS");

		// Generate a real signature
		byte[] generatedSignature = crypto.sign(content, privateKey);
		String generatedSignatureBase64 = Base64.getEncoder().encodeToString(generatedSignature);
		System.out.println("[INFO] Generated signature (Base64):");
		System.out.println(generatedSignatureBase64);
		System.out.println(generatedSignatureBase64.equals(validSignatureBase64) ? "[OK] Signature correctly generated"
				: "[FAIL] Incorrect signature generation");

		// VALID SIGNATURE TEST

		byte[] validSignature = Base64.getDecoder().decode(validSignatureBase64);
		System.out.println(crypto.verifySignature(validSignature, content, publicKey) ? "[OK] Valid signature accepted"
				: "[FAIL] Valid signature rejected");

		// INVALID SIGNATURE TEST

		String invalidSignatureBase64 = validSignatureBase64.replaceFirst("o", "b");
		byte[] invalidSignature = Base64.getDecoder().decode(invalidSignatureBase64);
		boolean invalidSignatureResult = crypto.verifySignature(invalidSignature, content, publicKey);
		System.out.println(
				!invalidSignatureResult ? "[OK] Invalid signature rejected" : "[FAIL] Invalid signature accepted");

		System.out.println("ENCRYPTION TESTS");

		String plaintext = "hello there my friends";
		String validCiphertextBase64 = "RBHuFc9D0ZmyBrt7/q5llXHt3X54xV5y+z1Xi/DYDWw/5zpMI9e+KsOICEnqhLvaa3z+vsEgx8trwKanLovL/ix5hiGsGSP5LesZvRg1QxLS7o9EvLSE+fy/zgosQ0y2QtlE3GNU1mhB3zPKWXCCURaG6DNEwBy7IlIDG6sfZ+/isb/9ulheMzDKCo6aiwJiWf1g5ULSg9v3CSCWQLcgzPoCjADajnFy3NFZCpviEaZU/GYv5F78DdJ14HmGzEW26uHfo1WtiyrY6DKgZjf/2o9muqFCjUTt3K+7qiNtW+vwv78GnMGk0gdsq9qPOuTC+68hVTSXH2a5o5Ub7lK6VA==";

		byte[] generatedCiphertext = crypto.encrypt(plaintext, publicKey);

		System.out.println("[INFO] Generated ciphertext (Base64):");
		System.out.println(Base64.getEncoder().encodeToString(generatedCiphertext));

		// VALID DECRYPTION TEST
		byte[] validCiphertext = Base64.getDecoder().decode(validCiphertextBase64);
		String decrypted = crypto.decrypt(validCiphertext, privateKey);
		System.out.println(plaintext.equals(decrypted) ? "[OK] Valid ciphertext decrypted correctly"
				: "[FAIL] Decrypted text mismatch");
		System.out.println("[INFO] Decrypted value: " + decrypted);

		// INVALID DECRYPTION TEST
		String invalidCiphertextBase64 = "RBHuFc9D0ZmyBrt7/q5llXHt3X54xV5y+z1Xi/DYDWw/5zpMI9e+KsOICEnqhLvaa3z+vsEgx8trwKanLovL/ix5hiGsGSP5LesZvRg1QxLS7o9EvLSE+fy/zgosQ0y2QtlE3GNU1mhB3zPKWXCCURaG6DNEwBy7IlIDG6sfZ+/isb/9ulheMzDKCo6aiwJiWf1g5ULSg9v3CSCWQLcgzPoCjADajnFy3NFZCpviEaZU/GYv5F78DdJ14HmGzEW26uHfo1WtiyrY6DKgZjf/2o9muqFCjUTt3K+7qiNtW+vwv78GnMGk0gdsq9qPOuTC+68hVTSXH2a5o5Ub7lK6VA==";
		try {
			byte[] invalidCiphertext = Base64.getDecoder().decode(invalidCiphertextBase64);
			String decrypted2 = crypto.decrypt(invalidCiphertext, privateKey);
			System.out.println("[FAIL] Invalid ciphertext decrypted unexpectedly");
			System.out.println("[INFO] Result: " + decrypted2);
		} catch (Exception e) {
			System.out.println("[OK] Invalid ciphertext rejected");
			System.out.println("[INFO] Exception: " + e.getMessage());
		}

		System.out.println("TESTS FINISHED");
	}
}
