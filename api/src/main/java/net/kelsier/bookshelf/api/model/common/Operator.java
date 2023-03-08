package net.kelsier.bookshelf.api.model.common;

/**
 * Operators used for performing a query.
 * The value is used by the user for creating a query and the label is used as part of a database query
 */
public enum Operator {
    EQ("="),
    NEQ("!="),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    LIKE("ILIKE"),
    UNLIKE("NOT ILIKE");

    private final String label;

    Operator(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
