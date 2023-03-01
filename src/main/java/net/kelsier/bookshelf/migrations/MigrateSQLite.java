package net.kelsier.bookshelf.migrations;

import net.kelsier.bookshelf.migrations.dao.*;
import net.kelsier.bookshelf.migrations.exception.MigrationException;
import net.kelsier.bookshelf.migrations.model.*;
import org.jdbi.v3.core.Jdbi;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MigrateSQLite {
    private final Jdbi databaseConnection;
    private final String sqliteDatabase;

    public MigrateSQLite(final Jdbi databaseConnection, final String sqliteDatabase) {
        this.databaseConnection = databaseConnection;
        this.sqliteDatabase = sqliteDatabase;
    }
    public void migrate() throws MigrationException {
        authors();
        books();
        comments();
        data();
        identifiers();
        languages();
        publishers();
        ratings();
        series();
        tags();
        bookAuthorLink();
        bookLanguageLink();
        bookPublisherLink();
        bookRatingsLink();
        bookSeriesLink();
        booksTagsLink();
        customColumns();
        createCustomColumns();
        populateCustomColumns();
    }

    public void drop() {
        purgeCustomColumns();
        purgeBookSeriesLink();
        purgeBooksTagsLink();
        purgeBookRatingsLink();
        purgePublishers();
        purgeSeries();
        purgeTags();
        purgeRatings();
        purgeAuthors();
        purgeBooks();
        purgeComments();
        purgeData();
        purgeIdentifiers();
        purgeLanguages();
        purgeBookAuthorLink();
        purgeBookLanguageLink();
        purgeBookPublisherLink();
        purgeCustomColumn();
    }

    private Connection connect() {
        final String url = MessageFormat.format("jdbc:sqlite:{0}", sqliteDatabase);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (final SQLException e) {
            throw new MigrationException("Unable to connect to source database", e);
        }
        return conn;
    }


    public void createCustomColumns() {
        String sql = "select * from custom_columns;";

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
            throw new MigrationException("Unable to create custom columns", e);
        }
    }

    public void populateCustomColumns() {
        final List<Integer> customColumns = new ArrayList<>();

        CustomColumnDAO customColumnDAO = databaseConnection.onDemand(CustomColumnDAO.class);
        CustomColumnLinkDAO customColumnLinkDAO = databaseConnection.onDemand(CustomColumnLinkDAO.class);

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery( "select * from custom_columns;")) {

            while (rs.next()) {
                final Integer id = rs.getInt("id");
                final Boolean normalized = rs.getBoolean("normalized");
                if (normalized) {
                    customColumns.add(id);
                }
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to read from custom columns", e);
        }

        customColumns.forEach((Integer columnId) -> {
            final String table = MessageFormat.format("custom_column_{0}", columnId);
            final String sql = MessageFormat.format("select * from {0};", table);
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();

                 ResultSet rs = stmt.executeQuery( sql)) {

                while (rs.next()) {
                    final String value = rs.getString("value");
                    customColumnDAO.insert(table, new CustomColumn(0, value));

                }
            } catch (SQLException e) {
                throw new MigrationException("Unable to populate custom column", e);
            }
        });

        customColumns.forEach((Integer columnId) -> {
            final String table = MessageFormat.format("custom_column_{0}_link", columnId);
            final String sql = MessageFormat.format("select * from books_{0};", table);
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();

                 ResultSet rs = stmt.executeQuery( sql)) {

                while (rs.next()) {
                    final Integer book = rs.getInt("book");
                    final Integer value = rs.getInt("value");

                    customColumnLinkDAO.insert(table, new CustomColumnLink(0, book, value));

                }
            } catch (SQLException e) {
                throw new MigrationException("Unable to populate custom column link", e);
            }
        });
    }

    public void bookSeriesLink() {
        String sql = " select * from books_series_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookSeriesLinkDAO bookSeriesLinkDAO = databaseConnection.onDemand(BookSeriesLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer series = rs.getInt("series");

                final BookSeriesLink bookSeriesLink = new BookSeriesLink(0, book, series);

                bookSeriesLinkDAO.insert(bookSeriesLink);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book series link", e);
        }
    }

    public void booksTagsLink() {
        String sql = " select * from books_tags_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookTagLinkDAO bookTagLinkDAO = databaseConnection.onDemand(BookTagLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer rating = rs.getInt("tag");

                final BookTagLink bookTagLink = new BookTagLink(0, book, rating);

                bookTagLinkDAO.insert(bookTagLink);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book tags link", e);
        }
    }

    public void customColumns() {
        String sql = " select * from custom_columns;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final CustomColumnsDAO customColumnsDAO = databaseConnection.onDemand(CustomColumnsDAO.class);

            while (rs.next()) {
                final String label = rs.getString("label");
                final String name = rs.getString("name");
                final String datatype = rs.getString("datatype");
                final String display = rs.getString("display");
                final Boolean isMultiple = rs.getBoolean("is_multiple");
                final Boolean normalized = rs.getBoolean("normalized");

                final CustomColumns customColumns = new CustomColumns(0, label, name, datatype, display,
                        isMultiple, normalized);

                customColumnsDAO.insert(customColumns);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate custom columns", e);
        }
    }

    public void bookRatingsLink() {
        String sql = " select * from books_ratings_link;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final BookRatingLinkDAO bookRatingLinkDAO = databaseConnection.onDemand(BookRatingLinkDAO.class);

            while (rs.next()) {
                final Integer book = rs.getInt("book");
                final Integer rating = rs.getInt("rating");

                final BookRatingLink bookRatingLink = new BookRatingLink(0, book, rating);

                bookRatingLinkDAO.insert(bookRatingLink);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate books ratings link", e);
        }
    }

    public void publishers() {
        String sql = " select * from publishers;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final PublisherDAO publisherDAO = databaseConnection.onDemand(PublisherDAO.class);

            while (rs.next()) {
                final String name = rs.getString("name");
                final String sort = rs.getString("sort");

                final Publisher publisher = new Publisher(0, name, sort);

                publisherDAO.insert(publisher);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate publishers", e);
        }
    }

    public void series() {
        String sql = " select * from series;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final SeriesDAO seriesDAO = databaseConnection.onDemand(SeriesDAO.class);

            while (rs.next()) {
                final String name = rs.getString("name");
                final String sort = rs.getString("sort");

                final Series series = new Series(0, name, sort);

                seriesDAO.insert(series);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate series", e);
        }
    }

    public void tags() {
        String sql = " select * from tags;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final TagDAO tagDAO = databaseConnection.onDemand(TagDAO.class);

            while (rs.next()) {
                final String name = rs.getString("name");

                final Tag tag = new Tag(0, name);

                tagDAO.insert(tag);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate tags", e);
        }
    }
    public void ratings() {
        String sql = " select * from ratings;";

        try (Connection conn = this.connect();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(sql)) {

            final RatingDAO ratingDAO = databaseConnection.onDemand(RatingDAO.class);

            while (rs.next()) {
                final Integer ratingValue = rs.getInt("rating");

                final Rating rating = new Rating(0, ratingValue);

                ratingDAO.insert(rating);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate ratings", e);
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
            throw new MigrationException("Unable to populate authors", e);
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
            throw new MigrationException("Unable to populate books", e);
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
            throw new MigrationException("Unable to populate comments", e);
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
                final String name = rs.getString("name");

                final Data data = new Data(0, book, format, size, name);
                dataDAO.insert(data);

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate data", e);
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
            throw new MigrationException("Unable to populate identifiers", e);
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
            throw new MigrationException("Unable to populate languages", e);
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
            throw new MigrationException("Unable to populate book author link", e);
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
            throw new MigrationException("Unable to populate book languages link", e);
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
            throw new MigrationException("Unable to populate book publishers link", e);
        }
    }

    //

    public void purgeCustomColumn() {
        CustomColumnsDAO customColumnsDao = databaseConnection.onDemand(CustomColumnsDAO.class);
        final List<CustomColumns> customColumns = customColumnsDao.get();

        customColumns.forEach((CustomColumns column) -> {
            final String customColumn = MessageFormat.format("custom_column_{0}", column.getId());
            final String customColumnLink = MessageFormat.format("custom_column_{0}_link", column.getId());
            databaseConnection.onDemand(CustomColumnDAO.class).drop(customColumn);
            databaseConnection.onDemand(CustomColumnLinkDAO.class).drop(customColumnLink);
        });
    }

    public void purgeBookSeriesLink() {
        databaseConnection.onDemand(BookSeriesLinkDAO.class).purge();
    }

    public void purgeBooksTagsLink() {
        databaseConnection.onDemand(BookTagLinkDAO.class).purge();
    }

    public void purgeCustomColumns() {
        databaseConnection.onDemand(CustomColumnsDAO.class).purge();
    }

    public void purgeBookRatingsLink() {
        databaseConnection.onDemand(BookRatingLinkDAO.class).purge();
    }

    public void purgePublishers() {
        databaseConnection.onDemand(PublisherDAO.class).purge();
    }

    public void purgeSeries() {
        databaseConnection.onDemand(SeriesDAO.class).purge();
    }

    public void purgeTags() {
        databaseConnection.onDemand(TagDAO.class).purge();
    }
    public void purgeRatings() {
        databaseConnection.onDemand(RatingDAO.class).purge();
    }

    public void purgeAuthors() {
        databaseConnection.onDemand(AuthorDAO.class).purge();
    }

    public void purgeBooks() {
         databaseConnection.onDemand(BookDAO.class).purge();
    }

    public void purgeComments() {
        databaseConnection.onDemand(CommentDAO.class).purge();
    }

    public void purgeData() {
        databaseConnection.onDemand(DataDAO.class).purge();
    }

    public void purgeIdentifiers() {
        databaseConnection.onDemand(IdentifierDAO.class).purge();
    }

    public void purgeLanguages() {
        databaseConnection.onDemand(LanguageDAO.class).purge();
    }

    public void purgeBookAuthorLink() {
        databaseConnection.onDemand(BookAuthorLinkDAO.class).purge();
    }

    public void purgeBookLanguageLink() {
        databaseConnection.onDemand(BookLanguageLinkDAO.class).purge();
    }

    public void purgeBookPublisherLink() {
        databaseConnection.onDemand(BookPublisherLinkDAO.class).purge();
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
