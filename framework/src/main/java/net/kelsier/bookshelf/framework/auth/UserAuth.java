package net.kelsier.bookshelf.framework.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User authentication principal
 */
public class UserAuth implements Principal, Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * Username for the login operation.
     */
    @NotNull
    @Size(min = 1, max = 255)
    private String username;
    /**
     * User's password.
     */
    @JsonIgnore
    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    public UserAuth() {
    }

    /**
     * Constructor to create users.
     *
     * @param username the username.
     * @param password the password.
     */
    public UserAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.id,
                this.username,
                this.password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAuth other = (UserAuth) obj;
        return Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password)
                && Objects.equals(this.id, other.id);
    }


    /**
     * Method implementation from Principal interface.
     *
     * @return The name of the Principal.
     */
    @Override
    public String getName() {
        return getUsername();
    }

}
