package net.kelsier.bookshelf.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to provide common shared functionality
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public final class Helper {
    private Helper() {
        throw new IllegalStateException("Utility class, should not be instantiated.");
    }

    /**
     * Read data from a resource and return it as a string
     *
     * @param name the name of the file to read
     *
     * @return The file contents as a string
     */
    public static String readFromInputStream(final String name) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(name);

            StringBuilder resultStringBuilder = new StringBuilder();
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            return resultStringBuilder.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Read data from a resource and return it as a string
     *
     * @param name the name of the file to read
     *
     * @return The file contents as a JSON string
     */
    public static String readJsonFromInputStream(final String name) {

        return readFromInputStream(name).replace("\n", "").replace(" ", "");
    }
}
