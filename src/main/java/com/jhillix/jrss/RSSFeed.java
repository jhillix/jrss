package com.jhillix.jrss;

import org.jsoup.Jsoup;
import org.unbescape.html.HtmlEscape;


/**
 * Represents the <item> of an RSS 2.0 feed. Refer to: http://www.w3schools.com/webservices/rss_item.asp
 *
 * @author jhillix
 */
public class RSSFeed {

    private String title;

    private String link;

    private String description;

    private String pubDate;

    public RSSFeed() {}

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public void setDescription(final String description) {
        // If a "title" attribute exists then grab the contents. Some sites embed images in the RSS "description" tag >_<
        if (description.contains("title=")) {
            int start = (description.indexOf("title=\"") + 7);
            int end = (description.indexOf("\"", start + 7));
            this.description = HtmlEscape.unescapeHtml(description.substring(start, end));
        } else {
            // Otherwise, take the raw input and strip all HTML.
            // new HtmlToPlainText().getPlainText(Jsoup.parse(html));
            this.description = Jsoup.parse(description).text();
        }
    }

    public void setPubDate(final String pubDate) {
        this.pubDate = pubDate;
    }
}
