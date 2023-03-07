# Bookshelf

![](https://github.com/KelsierLuthadel/meta-books/workflows/Build/badge.svg)

## Encryption
The recommended approach to storing sensitive bookData is by using encryption at rest. 

### Encrypting passwords in a configuration file
When a class defines a password field as `@Encrypted`, the associated yaml field can be used to either store the password 
as an encrypted value, or in plain text format.

The default algorithm used for encryption here is PBE with MD5 and Triple DES.

**Example**

The following example shows an encrypted database password in `DatasourceConfiguration.yaml`
```yaml
# the name of your JDBC driver
driverClass: org.postgresql.Driver

# the username
user: bookshelf

# the password
password: ya8ftMwhAAhn1/dxuQtENf7bNJij7rle

# the JDBC URL
url: jdbc:postgresql://localhost:5432/bookshelf
```

The above example can also be used, but with the password in plain text format
```yaml
# the name of your JDBC driver
driverClass: org.postgresql.Driver

# the username
user: bookshelf

# the password
password: password

# the JDBC URL
url: jdbc:postgresql://localhost:5432/bookshelf
```
 
#### Encrypting a password
The admin port (default: 1310) provides `tasks/encrypt` so that a password can be encrypted for use in a config field.

**Example**

request:
```
curl --location --request POST 'http://localhost:1311/tasks/encrypt?plaintext=password'
```

response:
```
[{"Ciphertext":"VIQ6gOOJ57hdAfocHT2DIgpAOaeGGG8s"}]
```
*Note: Providing the same plain-text password multiple times will produce a different password each time*

#### Encryption password
By default, the master encryption password is stored in `bookshelf.yaml` and this is used for encryption and decryption, 
however this is not considered safe. Therefore, this can be overridden by supplying the following system property when 
starting the server:

```bash
java -DCIPHER=cipher-password -jar target/metabooks.jar server ./config/bookshelf.yaml
```

### Encrypting passwords in the database
By default, all passwords that are stored in the database are encrypted using one-way encryption. One-way encryption provides 
a stronger encryption method with no way to decrypt the password. Passwords can be verified, but never decrypted. The 
configuration for database password encryption is in `EncryptionConfiguration.yaml`

**Example:**
```yaml
algorithm: SHA-256
saltSize: 16
iterations: 100000
```

## Recreate database
```
drop database bookshelf
UPDATE databasechangelog SET MD5SUM = NULL;
create database bookshelf
\c bookshelf
GRANT ALL ON SCHEMA public TO bookshelf;
```

