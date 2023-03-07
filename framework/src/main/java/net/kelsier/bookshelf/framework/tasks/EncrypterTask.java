package net.kelsier.bookshelf.framework.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.servlets.tasks.Task;
import net.kelsier.bookshelf.framework.encryption.Cipher;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * This class defines a Dropwizard task which can be used to encrypt data for config.
 */
public final class EncrypterTask extends Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncrypterTask.class);
    private final Cipher cipher;

    /**
     * Constructor.
     * The name given to the parent class is used in the path at which the task will be accessible
     *
     * @param cipher The initialised Cipher to use when encrypting
     */
    public EncrypterTask(final Cipher cipher) {
        super("encrypt");
        this.cipher = cipher;
    }

    /**
     * Execute is called when a POST request is received by Dropwizard for the task.
     * Any parameters with key "plaintext" are encrypted and returned with the plain text in a JSON structure
     *
     * @param parameters The POST parameters (x-www-form-urlencoded) in key-value form
     * @param output     Content written to this writer will be returned in the body of the POST response
     * @throws IOException Throws if there is an issue writing output
     */
    @Override
    public void execute(final Map<String, List<String>> parameters, final PrintWriter output) throws IOException {
        LOGGER.info("Performing encryption");
        final List<String> plaintexts = parameters.get("plaintext");
        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(output, plaintexts.stream().map(
            (String text) -> {
                try {
                    return Map.of("Ciphertext", cipher.encrypt(text));
                } catch (final EncryptionOperationNotPossibleException e) {
                    LOGGER.error("Unable to encrypt: " + text, e);
                    return Map.of("Ciphertext", "ERROR! Unable to encrypt this value: " + e.getMessage());
                }
            }
        ).toArray());

        output.flush();
    }
}
