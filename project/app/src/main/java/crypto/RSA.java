package crypto;

// RSA-OAEP + RSA signature
public class RSA implements Crypto {
	public byte[] sign(byte[] buffer, String privateKey) {
		// TODO
	}

	public boolean verifySignature(byte[] signature, byte[] signedContent, String publicKey) {
		// TODO
	}

	public byte[] encrypt(String text, String publicKey) {
		// TODO
	}

	public String decrypt(byte[] cipherText, String privateKey) {
		// TODO
	}
}
