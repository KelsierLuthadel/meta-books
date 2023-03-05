package net.kelsier.bookshelf.api.model.common;

public interface ColumnLookup {
    String getField();
    String getValue();
    Operator getOperator();
}
