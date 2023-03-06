package net.kelsier.bookshelf.framework.util;

import org.jsoup.Jsoup;

public class HtmlHelper {

    public static String stripHtml(final String html) {
        return Jsoup.parse(html).text();
    }
}
