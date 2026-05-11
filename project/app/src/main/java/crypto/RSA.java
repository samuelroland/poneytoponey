package crypto;

import java.security.spec.MGF1ParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

// This implements a non production ready integration of RSA-OAEP for encryption + RSA for signature
// ChatGPT public free version was heavily used to integrate usage of all Java APIs
public class RSA implements Crypto {
	private Cipher cipher; // provides RSA encryption/decryption
	private OAEPParameterSpec oaepParams; // parameters for RSA-OAEP
	private Signature signer; // a way to sign with RSA

	public RSA() {
		try {
			this.cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			this.oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
					PSource.PSpecified.DEFAULT);
			this.signer = Signature.getInstance("SHA256withRSA");
		} catch (Exception e) {
			throw new RuntimeException("RSA parameters are wrong: " + e);
		}
	}

	@Override
	public byte[] sign(byte[] buffer, PrivateKey privateKey) {
		try {
			signer.initSign(privateKey);
			signer.update(buffer);
			return signer.sign();
		} catch (Exception e) {
			throw new RuntimeException("Failed to sign content", e);
		}
	}

	@Override
	public boolean verifySignature(byte[] signatureBytes, byte[] signedContent, PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(publicKey);
			signature.update(signedContent);
			return signature.verify(signatureBytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to verify signature", e);
		}
	}

	@Override
	public byte[] encrypt(String text, PublicKey publicKey) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
			return cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new RuntimeException("Failed to encrypt message with RSA-OAEP", e);
		}
	}

	@Override
	public String decrypt(byte[] cipherText, PrivateKey privateKey) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
			byte[] decrypted = cipher.doFinal(cipherText);
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Failed to decrypt message with RSA-OAEP", e);
		}
	}

	@Override
	public KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator;
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = new KeyPair(generator.generateKeyPair());
			return pair;
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate key pair for RSA ", e);
		}
	}
}
