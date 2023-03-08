package net.kelsier.bookshelf.api.model.common;

import net.kelsier.bookshelf.api.db.types.DataTypes;

/**
 * Interface used to map a search query to a database query
 */
public interface ColumnLookup {
    String getField();
    String getValue();
    String getLookupValue();
    Operator getOperator();
    DataTypes getDataType();
}
