package net.kelsier.bookshelf.framework.auth;

/**
 * This class holds the API Authorization constant
 */
public final class ApiAuthorization {

    /**
     * Swagger authorization name
     */
    public static final String SWAGGER_AUTHORIZATION = "auth";

    /**
     * Private constructor to hide the implicit public one
     */
    private ApiAuthorization() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }
}
