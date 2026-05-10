package crypto;

// RSA-OAEP + RSA signature
public class RSA implements Crypto {
	@Override
	public byte[] sign(byte[] buffer, String privateKey) {
		// TODO
		return "todo".getBytes();
	}

	@Override
	public boolean verifySignature(byte[] signature, byte[] signedContent, String publicKey) {
		// TODO
		return false;
	}

	@Override
	public byte[] encrypt(String text, String publicKey) {
		// TODO
		//
		return "todo".getBytes();
	}

	@Override
	public String decrypt(byte[] cipherText, String privateKey) {
		// TODO
		return "todo";
	}

	@Override
	public KeyPair generateKeyPair() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'generateKeyPair'");
	}
}
