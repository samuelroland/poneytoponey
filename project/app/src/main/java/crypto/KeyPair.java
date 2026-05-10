package crypto;

import java.security.PublicKey;

// A RSA keypair
public class KeyPair {
	private java.security.KeyPair pair;

	private KeyPair(java.security.KeyPair kp) {
		this.pair = kp;
	}

	public static KeyPair loadFromFile(String path) {
		// TODO
		// PublicKey pub = PublicKe
		// java.security.KeyPair = new java.security.KeyPair(pub, priv);
		// return this;
	}

	public static KeyPair generateNewPair() {
		// TODO
	}

	public void persistToFile() {
		// TODO
	}
}
