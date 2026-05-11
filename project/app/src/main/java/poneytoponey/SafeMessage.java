package poneytoponey;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import crypto.Crypto;
import crypto.RSA;

// A safe message is the equivalent of a Message but fully encrypted and signed.
// This ensures the confidentiality and authenticity of the message. 
public class SafeMessage implements Serializable {

    private byte[] encryptedText;
    private byte[] encryptedAuthor;
    private byte[] encryptedSenderTimestamp;
    private byte[] encryptedImportantFlag;
    private byte[] signature; // of the clear text + sender Timestamp

    public SafeMessage(Message message, PrivateKey ourPrivateKey, PublicKey recipientPublicKey) {
        Crypto crypto = new RSA();
        this.encryptedText = crypto.encrypt(message.getTexte(), recipientPublicKey);
        this.encryptedAuthor = crypto.encrypt(message.getAuthor(), recipientPublicKey);
        this.encryptedSenderTimestamp = crypto.encrypt(message.getSenderTimestamp().toString(), recipientPublicKey);
        this.encryptedImportantFlag = crypto.encrypt(message.getIsImportant().toString(), recipientPublicKey);

        this.signature = crypto.sign(generateBytesToSign(), ourPrivateKey);
    }

    private byte[] generateBytesToSign() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(this.encryptedText);
            outputStream.write(this.encryptedAuthor);
            outputStream.write(this.encryptedSenderTimestamp);
            outputStream.write(this.encryptedImportantFlag);
        } catch (Exception e) {
            throw new RuntimeException("Failed to concatenate bytes buffer");
        }
        return outputStream.toByteArray();
    }

    public Message verifyAndDecrypt(PublicKey authorPublicKey, PrivateKey ourPrivateKey) {
        Crypto crypto = new RSA();
        if (crypto.verifySignature(this.signature, generateBytesToSign(), authorPublicKey) == false) {
            return null;
        }
        String text = crypto.decrypt(this.encryptedText, ourPrivateKey);
        String author = crypto.decrypt(this.encryptedAuthor, ourPrivateKey);
        String timestamp = crypto.decrypt(this.encryptedSenderTimestamp, ourPrivateKey);
        String important = crypto.decrypt(this.encryptedImportantFlag, ourPrivateKey);
        Message m = new Message(text, Long.parseLong(timestamp), author, Boolean.parseBoolean(important));
        return m;
    }

}
