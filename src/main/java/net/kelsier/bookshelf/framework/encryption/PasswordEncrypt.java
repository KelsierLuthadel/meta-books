package net.kelsier.bookshelf.framework.encryption;

import net.kelsier.bookshelf.framework.config.EncryptionConfiguration;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.password.PasswordEncryptor;

public class PasswordEncrypt implements PasswordEncryptor {
    private static final String DEFAULT_ALGORITHM = "SHA-256";

    private static final Integer DEFAULT_SALT_SIZE = 16;

    private static final Integer DEFAULT_ITERATIONS = 100_000;

    private final StandardStringDigester digester;


    /**
     * Creates a new instance of <tt>PasswordEncrypt</tt> with default values
     *
     */
    public PasswordEncrypt() {
        super();
        this.digester = new StandardStringDigester();
        this.digester.setAlgorithm(DEFAULT_ALGORITHM);
        this.digester.setIterations(DEFAULT_ITERATIONS);
        this.digester.setSaltSizeBytes(DEFAULT_SALT_SIZE);
        this.digester.initialize();
    }

    /**
     * Creates a new instance of <tt>PasswordEncrypt</tt>
     *
     */
    public PasswordEncrypt(final EncryptionConfiguration cipherConfiguration) {
        super();
        this.digester = new StandardStringDigester();
        this.digester.setAlgorithm(cipherConfiguration.getAlgorithm());
        this.digester.setIterations(cipherConfiguration.getIterations());
        this.digester.setSaltSizeBytes(cipherConfiguration.getSaltSize());
        this.digester.initialize();
    }


    /**
     * Encrypts (digests) a password.
     *
     * @param password the password to be encrypted.
     * @return the resulting digest.
     * @see StandardStringDigester#digest(String)
     */
    public String encryptPassword(final String password) {
        return this.digester.digest(password);
    }


    /**
     * Checks an unencrypted (plain) password against an encrypted one
     * (a digest) to see if they match.
     *
     * @param plainPassword the plain password to check.
     * @param encryptedPassword the digest against which to check the password.
     * @return true if passwords match, false if not.
     * @see StandardStringDigester#matches(String, String)
     */
    public boolean checkPassword(final String plainPassword,
                                 final String encryptedPassword) {
        return this.digester.matches(plainPassword, encryptedPassword);
    }


}
