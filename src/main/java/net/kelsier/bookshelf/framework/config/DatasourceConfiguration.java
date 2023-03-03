package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import net.kelsier.bookshelf.framework.encryption.Encrypted;

public class DatasourceConfiguration extends DataSourceFactory {
    @Encrypted
    @JsonProperty("password")
    private String password;

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }
}
