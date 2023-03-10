package net.kelsier.bookshelf.migration;

import net.kelsier.bookshelf.api.db.dao.AuthorDAO;
import net.kelsier.bookshelf.api.db.dao.BookAuthorLinkDAO;
import net.kelsier.bookshelf.api.db.dao.BookDAO;
import net.kelsier.bookshelf.api.db.dao.BookLanguageLinkDAO;
import net.kelsier.bookshelf.api.db.dao.BookPublisherLinkDAO;
import net.kelsier.bookshelf.api.db.dao.BookRatingLinkDAO;
import net.kelsier.bookshelf.api.db.dao.BookSeriesLinkDAO;
import net.kelsier.bookshelf.api.db.dao.BookTagLinkDAO;
import net.kelsier.bookshelf.api.db.dao.CommentDAO;
import net.kelsier.bookshelf.api.db.dao.CustomColumnDAO;
import net.kelsier.bookshelf.api.db.dao.CustomColumnLinkDAO;
import net.kelsier.bookshelf.api.db.dao.CustomColumnsDAO;
import net.kelsier.bookshelf.api.db.dao.DataDAO;
import net.kelsier.bookshelf.api.db.dao.IdentifierDAO;
import net.kelsier.bookshelf.api.db.dao.LanguageDAO;
import net.kelsier.bookshelf.api.db.dao.PublisherDAO;
import net.kelsier.bookshelf.api.db.dao.RatingDAO;
import net.kelsier.bookshelf.api.db.dao.SeriesDAO;
import net.kelsier.bookshelf.api.db.dao.TagDAO;
import net.kelsier.bookshelf.api.db.model.Author;
import net.kelsier.bookshelf.api.db.model.Book;
import net.kelsier.bookshelf.api.db.model.links.BookAuthorLink;
import net.kelsier.bookshelf.api.db.model.BookData;
import net.kelsier.bookshelf.api.db.model.links.BookLanguageLink;
import net.kelsier.bookshelf.api.db.model.links.BookPublisherLink;
import net.kelsier.bookshelf.api.db.model.links.BookRatingLink;
import net.kelsier.bookshelf.api.db.model.links.BookSeriesLink;
import net.kelsier.bookshelf.api.db.model.links.BookTagLink;
import net.kelsier.bookshelf.api.db.model.Comment;
import net.kelsier.bookshelf.api.db.model.customcolumn.CustomColumn;
import net.kelsier.bookshelf.api.db.model.links.CustomColumnLink;
import net.kelsier.bookshelf.api.db.model.customcolumn.CustomColumns;
import net.kelsier.bookshelf.api.db.model.Identifier;
import net.kelsier.bookshelf.api.db.model.Language;
import net.kelsier.bookshelf.api.db.model.Publisher;
import net.kelsier.bookshelf.api.db.model.Rating;
import net.kelsier.bookshelf.api.db.model.Series;
import net.kelsier.bookshelf.api.db.model.Tag;
import net.kelsier.bookshelf.migration.exception.MigrationException;
import org.jdbi.v3.core.Jdbi;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MigrateSQLite {
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateSQLite.class);
    private final Jdbi databaseConnection;
    private final String sqliteDatabase;

    public MigrateSQLite(final Jdbi databaseConnection, final String sqliteDatabase) {
        this.databaseConnection = databaseConnection;
        this.sqliteDatabase = sqliteDatabase;
    }
    public void migrate() throws MigrationException {
        populateAuthors();
        populateBooks();
        populateComments();
        populateData();
        populateIdentifiers();
        populateLanguages();
        populatePublishers();
        populateRatings();
        populateSeries();
        populateTags();
        populateBookAuthorLink();
        populateBookLanguageLink();
        populateBookPublisherLink();
        populateBookRatingsLink();
        populateBookSeriesLink();
        populateBooksTagsLink();
        populateCustomColumns();
        createCustomColumn();
        populateCustomColumn();
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
        try {
            return DriverManager.getConnection(MessageFormat.format("jdbc:sqlite:{0}", sqliteDatabase));
        } catch (final SQLException e) {
            throw new MigrationException("Unable to connect to source database", e);
        }
    }


    public void createCustomColumn() {
        try {
            final List<Integer> columns = getColumns();

            CustomColumnDAO customColumnDAO = databaseConnection.onDemand(CustomColumnDAO.class);
            CustomColumnLinkDAO customColumnLinkDAO = databaseConnection.onDemand(CustomColumnLinkDAO.class);
            columns.forEach((Integer columnId) -> {
                customColumnDAO.create(MessageFormat.format("public.custom_column_{0}", columnId));
                customColumnLinkDAO.create(MessageFormat.format("public.custom_column_{0}_link", columnId));
            });
        } catch (MigrationException e) {
            throw new MigrationException("Unable to create custom columns", e);
        }
    }

    public void populateCustomColumn() {
        final List<Integer> customColumns = getColumns();
        populateCustom(customColumns);
        populateCustomLink(customColumns);
    }

    private void populateCustomLink(final List<Integer> customColumns) {
        CustomColumnLinkDAO customColumnLinkDAO = databaseConnection.onDemand(CustomColumnLinkDAO.class);
        customColumns.forEach((Integer columnId) -> {
            final String table = MessageFormat.format("custom_column_{0}_link", columnId);
            final String sql = MessageFormat.format("select * from books_{0};", table);
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();

                 ResultSet rs = stmt.executeQuery( sql)) {

                while (rs.next()) {
                    final Integer book = rs.getInt("book");
                    final Integer value = rs.getInt("value");

                    customColumnLinkDAO.insert(table, new CustomColumnLink(rs.getInt("id"), book, value));

                }
            } catch (SQLException e) {
                throw new MigrationException("Unable to populate custom column link", e);
            }
        });
    }

    private void populateCustom(final List<Integer> customColumns) {
        CustomColumnDAO customColumnDAO = databaseConnection.onDemand(CustomColumnDAO.class);
        customColumns.forEach((Integer columnId) -> {
            final String table = MessageFormat.format("custom_column_{0}", columnId);
            final String sql = MessageFormat.format("select * from {0};", table);
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();

                 ResultSet rs = stmt.executeQuery( sql)) {

                while (rs.next()) {
                    customColumnDAO.insert(table, new CustomColumn(rs.getInt("id"), rs.getString("value")));

                }
            } catch (SQLException e) {
                throw new MigrationException("Unable to populate custom column", e);
            }
        });
    }

    private List<Integer> getColumns() throws MigrationException {
        final List<Integer> customColumns = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery( "select * from custom_columns;")) {

            while (rs.next()) {
                if (rs.getBoolean("normalized")) {
                    customColumns.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to read from custom columns", e);
        }
        return customColumns;
    }

    public void populateBookSeriesLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_series_link;")) {
            final BookSeriesLinkDAO bookSeriesLinkDAO = databaseConnection.onDemand(BookSeriesLinkDAO.class);

            while (rs.next()) {
                bookSeriesLinkDAO.insert(new BookSeriesLink(rs.getInt("id"), rs.getInt("book"), rs.getInt("series")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book series link", e);
        }
    }

    public void populateBooksTagsLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_tags_link;")) {
            final BookTagLinkDAO bookTagLinkDAO = databaseConnection.onDemand(BookTagLinkDAO.class);

            while (rs.next()) {
                bookTagLinkDAO.insert(new BookTagLink(rs.getInt("id"), rs.getInt("book"), rs.getInt("tag")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book tags link", e);
        }
    }

    public void populateCustomColumns() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from custom_columns;")) {
            final CustomColumnsDAO customColumnsDAO = databaseConnection.onDemand(CustomColumnsDAO.class);

            while (rs.next()) {
                customColumnsDAO.insert(new CustomColumns(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getString("name"),
                        rs.getString("datatype"),
                        rs.getString("display"),
                        rs.getBoolean("is_multiple"),
                        rs.getBoolean("normalized")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate custom columns", e);
        }
    }

    public void populateBookRatingsLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_ratings_link;")) {
            final BookRatingLinkDAO bookRatingLinkDAO = databaseConnection.onDemand(BookRatingLinkDAO.class);

            while (rs.next()) {
                bookRatingLinkDAO.insert(new BookRatingLink(rs.getInt("id"), rs.getInt("book"), rs.getInt("rating")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate books ratings link", e);
        }
    }

    public void populatePublishers() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from publishers;")) {
            final PublisherDAO publisherDAO = databaseConnection.onDemand(PublisherDAO.class);

            while (rs.next()) {
                publisherDAO.insert(new Publisher(rs.getInt("id"), rs.getString("name"), rs.getString("sort")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate publishers", e);
        }
    }

    public void populateSeries() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from series;")) {
            final SeriesDAO seriesDAO = databaseConnection.onDemand(SeriesDAO.class);

            while (rs.next()) {
                seriesDAO.insert(new Series(rs.getInt("id"), rs.getString("name"), rs.getString("sort")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate series", e);
        }
    }

    public void populateTags() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from tags;")) {
            final TagDAO tagDAO = databaseConnection.onDemand(TagDAO.class);

            while (rs.next()) {
                tagDAO.insert(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate tags", e);
        }
    }
    public void populateRatings() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from ratings;")) {
            final RatingDAO ratingDAO = databaseConnection.onDemand(RatingDAO.class);

            while (rs.next()) {
                ratingDAO.insert(new Rating(rs.getInt("id"), rs.getInt("rating")));

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate ratings", e);
        }
    }


    public void populateAuthors() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from authors;")) {
            final AuthorDAO authorDAO = databaseConnection.onDemand(AuthorDAO.class);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String sort = rs.getString("sort");


                authorDAO.insert(new Author(id, name, sort));
            }

        } catch (SQLException e) {
            throw new MigrationException("Unable to populate authors", e);
        }
    }

    public void populateBooks() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books;")) {
            final BookDAO bookDAO = databaseConnection.onDemand(BookDAO.class);

            LOGGER.info("Migrating Books");
            while (rs.next()) {

                int id =  rs.getInt("id");
                String title =  rs.getString("title");

                bookDAO.insert(new Book(
                       id,
                       title,
                        rs.getString("sort"),
                        parseDateField(rs.getString("timestamp")),
                        parseDateField(rs.getString("pubdate")),
                        rs.getInt("series_index"),
                        rs.getString("isbn"),
                        rs.getString("path"),
                        rs.getBoolean("has_cover"),
                        parseDateField(rs.getString("last_modified"))));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate books", e);
        }
    }


    public void populateComments() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from comments;")) {
            final CommentDAO commentDao = databaseConnection.onDemand(CommentDAO.class);

            while (rs.next()) {
                commentDao.insert(new Comment(rs.getInt("id"), rs.getInt("book"), rs.getString("text")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate comments", e);
        }
    }

    public void populateData() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from data;")) {
            final DataDAO dataDAO = databaseConnection.onDemand(DataDAO.class);

            while (rs.next()) {
                dataDAO.insert(new BookData(rs.getInt("id"),
                        rs.getInt("book"),
                        rs.getString("format"),
                        rs.getInt("uncompressed_size"),
                        rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate data", e);
        }
    }

    public void populateIdentifiers() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from identifiers;")) {
            final IdentifierDAO identifierDAO = databaseConnection.onDemand(IdentifierDAO.class);

            while (rs.next()) {
                identifierDAO.insert(new Identifier(
                        rs.getInt("id"),
                        rs.getInt("book"),
                        rs.getString("type"),
                        rs.getString("val")));

            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate identifiers", e);
        }
    }

    public void populateLanguages() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from languages;")) {
            final LanguageDAO languageDAO = databaseConnection.onDemand(LanguageDAO.class);

            while (rs.next()) {
                languageDAO.insert(new Language(rs.getInt("id"), rs.getString("lang_code")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate languages", e);
        }
    }

    public void populateBookAuthorLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_authors_link;")) {
            final BookAuthorLinkDAO bookAuthorLinkDAO = databaseConnection.onDemand(BookAuthorLinkDAO.class);

            while (rs.next()) {
                bookAuthorLinkDAO.insert(new BookAuthorLink(
                        rs.getInt("id"), rs.getInt("book"), rs.getInt("author")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book author link", e);
        }
    }

    public void populateBookLanguageLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_languages_link;")) {
            final BookLanguageLinkDAO bookLanguageLinkDAO = databaseConnection.onDemand(BookLanguageLinkDAO.class);

            while (rs.next()) {
                bookLanguageLinkDAO.insert(new BookLanguageLink(
                        rs.getInt("id"), rs.getInt("book"), rs.getInt("lang_code")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book languages link", e);
        }
    }

    public void populateBookPublisherLink() {
        try (Connection conn = this.connect();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select * from books_publishers_link;")) {
            final BookPublisherLinkDAO bookPublisherLinkDAO = databaseConnection.onDemand(BookPublisherLinkDAO.class);

            while (rs.next()) {
                bookPublisherLinkDAO.insert( new BookPublisherLink(
                        rs.getInt("id"), rs.getInt("book"), rs.getInt("publisher")));
            }
        } catch (SQLException e) {
            throw new MigrationException("Unable to populate book publishers link", e);
        }
    }

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
