package crypto;

import java.security.PublicKey;

// A RSA keypair
public class KeyPair {
	private java.security.KeyPair pair;

	public KeyPair(java.security.KeyPair kp) {
		this.pair = kp;
	}

	public PublicKey getPublic() {
		return pair.getPublic();
	}

	public static KeyPair loadFromFile(String path) {
		// TODO
		// PublicKey pub = PublicKe
		// java.security.KeyPair = new java.security.KeyPair(pub, priv);
		// return this;
	}

	public void persistToFile() {
		// TODO
	}
}
