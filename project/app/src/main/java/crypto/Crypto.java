package crypto;

// Generic Crypto interface for all features we need to access in the application.
// Concrete algorithms are integrated in concrete implementations.
public interface Crypto {
	public byte[] sign(byte[] buffer, String privateKey);

	public boolean verifySignature(byte[] signature, byte[] signedContent, String publicKey);

	public byte[] encrypt(String text, String publicKey);

	public String decrypt(byte[] cipherText, String privateKey);

	public KeyPair generateKeyPair();
}
