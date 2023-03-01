package net.kelsier.bookshelf.migrations;

import net.kelsier.bookshelf.migrations.dao.*;
import net.kelsier.bookshelf.migrations.model.*;
import org.jdbi.v3.core.Jdbi;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;
import java.text.MessageFormat;
import java.util.Locale;


public class MigrateSQLite {
    private final Jdbi databaseConnection;
    private final String sqliteDatabase;

    public MigrateSQLite(final Jdbi databaseConnection, final String sqliteDatabase) {
        this.databaseConnection = databaseConnection;
        this.sqliteDatabase = sqliteDatabase;
    }
    public void migrate() {
//        final Connection conn = connect(database);
        customColumns();
    }

    private Connection connect() {
        final String url = MessageFormat.format("jdbc:sqlite:{0}", sqliteDatabase);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void customColumns() {
        String sql = " select * from custom_columns;";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            CustomColumnDAO customColumnDAO = databaseConnection.onDemand(CustomColumnDAO.class);
            CustomColumnLinkDAO customColumnLinkDAO = databaseConnection.onDemand(CustomColumnLinkDAO.class);
            // loop through the result set
            while (rs.next()) {
                final Integer id = rs.getInt("id");
                String table = MessageFormat.format("custom_column_{0}", id);
                customColumnDAO.create(table);
                table = MessageFormat.format("custom_column_{0}_link", id);
                customColumnLinkDAO.create(table);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void authors() {
        String sql = " select * from authors;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final AuthorDAO authorDAO = databaseConnection.onDemand(AuthorDAO.class);

            while (rs.next()) {
                final String name = rs.getString("name");
                final String sort = rs.getString("sort");
                final Author author = new Author(0, name, sort);
                authorDAO.insert(author);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void books() {
        String sql = " select * from books;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookDAO bookDAO = databaseConnection.onDemand(BookDAO.class);

            while (rs.next()) {
                final String title = rs.getString("title");
                final String sort = rs.getString("sort");
                final Timestamp dateAdded = parseDateField(rs.getString("timestamp"));
                final Timestamp publicationDate = parseDateField(rs.getString("pubdate"));
                final Double series_index = rs.getDouble("series_index");
                final String isbn = rs.getString("isbn");
                final String path = rs.getString("path");
                final Boolean hasCover = rs.getBoolean("has_cover");
                final Timestamp lastModified = parseDateField(rs.getString("last_modified"));

                final Book book = new Book(0, title, sort, dateAdded, publicationDate, series_index, isbn, path, hasCover, lastModified);
                bookDAO.insert(book);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void comments() {
        String sql = " select * from comments;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final CommentDAO commentDao = databaseConnection.onDemand(CommentDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final String text = rs.getString("text");
                final Comment comment = new Comment(0, book, text);
                commentDao.insert(comment);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void data() {
        String sql = " select * from data;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final DataDAO dataDAO = databaseConnection.onDemand(DataDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final String format = rs.getString("format");
                final Integer size = rs.getInt("uncompressed_size");
                final String text = rs.getString("text");

                final Data data = new Data(0, book, format, size, text);
                dataDAO.insert(data);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void identifiers() {
        String sql = " select * from identifiers;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final IdentifierDAO identifierDAO = databaseConnection.onDemand(IdentifierDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final String type = rs.getString("type");
                final String val = rs.getString("val");

                final Identifier data = new Identifier(0, book, type, val);
                identifierDAO.insert(data);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void languages() {
        String sql = " select * from languages;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final LanguageDAO languageDAO = databaseConnection.onDemand(LanguageDAO.class);

            while (rs.next()) {
                final String languageCode = rs.getString("lang_code");

                final Language data = new Language(0, languageCode);
                languageDAO.insert(data);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void bookAuthorLink() {
        String sql = " select * from books_authors_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookAuthorLinkDAO bookAuthorLinkDAO = databaseConnection.onDemand(BookAuthorLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer author = rs.getInt("author");

                final BookAuthorLink bookAuthorLink = new BookAuthorLink(0, book, author);
                bookAuthorLinkDAO.insert(bookAuthorLink);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void bookLanguageLink() {
        String sql = " select * from books_languages_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookLanguageLinkDAO bookLanguageLinkDAO = databaseConnection.onDemand(BookLanguageLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer languageCode = rs.getInt("lang_code");

                final BookLanguageLink bookLanguageLink = new BookLanguageLink(0, book, languageCode);
                bookLanguageLinkDAO.insert(bookLanguageLink);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void bookPublisherLink() {
        String sql = " select * from books_publishers_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookPublisherLinkDAO bookPublisherLinkDAO = databaseConnection.onDemand(BookPublisherLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer publisher = rs.getInt("publisher");

                final BookPublisherLink bookPublisherLink = new BookPublisherLink(0, book, publisher);
                bookPublisherLinkDAO.insert(bookPublisherLink);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static Timestamp parseDateTime(final String dateString) {
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ssZ").withLocale(Locale.ENGLISH).withOffsetParsed();
        final DateTime dateTime = DateTime.parse(dateString, dtf);
        return new Timestamp(dateTime.getMillis());
    }

    private static Timestamp parseModifiedDateTime(final String dateString) {
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSSZ").withLocale(Locale.ENGLISH).withOffsetParsed();
        final DateTime dateTime = DateTime.parse(dateString, dtf);
        return new Timestamp(dateTime.getMillis());
    }

    private static Timestamp parseDateField(final String dateString) {
        try {
            return parseDateTime(dateString);
        } catch(final Exception e) {
            return parseModifiedDateTime(dateString);
        }
    }
}
