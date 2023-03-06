package net.kelsier.bookshelf.framework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import net.kelsier.bookshelf.framework.encryption.Encrypted;

import javax.annotation.Nullable;

public class DatasourceConfiguration extends DataSourceFactory {
    @Encrypted
    @JsonProperty("password")
    private String password;

    @Override
    public void setPassword(final String password) {
        this.password = password;
        super.setPassword(password);
    }

    @Override
    @JsonProperty
    @Nullable
    public String getPassword() {
        return password;
    }
}
