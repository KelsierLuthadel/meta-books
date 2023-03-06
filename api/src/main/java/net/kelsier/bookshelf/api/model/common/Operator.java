package net.kelsier.bookshelf.api.model.common;

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
