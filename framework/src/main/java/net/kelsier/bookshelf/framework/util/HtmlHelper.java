package net.kelsier.bookshelf.framework.util;

import org.jsoup.Jsoup;

public final class HtmlHelper {

    private HtmlHelper() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }

    public static String stripHtml(final String html) {
        return Jsoup.parse(html).text();
    }
}
