package com.jhillix.jrss;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * Read the feed.
 *
 * @author jhillix
 */
public class RSSReader {

    private static Logger LOG = Logger.getLogger(RSSReader.class);

    private Document document;

    public RSSReader() {}

    /**
     * Takes an InputStream and parses out the important bits to then be displayed to the user via
     * the console.
     *
     * @param inputStream of the RSS feed to read
     */
    public List<RSSFeed> read(final InputStream inputStream) {
        List<RSSFeed> feeds = new ArrayList<>();

        try {
            LOG.info("Creating XML reader.");
            System.out.println(ansi().fgBright(GREEN).a("Creating XML reader.").reset());
            document = new SAXReader().read(inputStream);

            List<Node> nodes = document.selectNodes("/rss/channel/item");

            LOG.info("Parsing RSS feed.");
            System.out.println(ansi().fgBright(GREEN).a("Parsing RSS feed.").reset());

            if (!nodes.isEmpty()) {
                for (Node node : nodes) {
                    RSSFeed feed = new RSSFeed();

                    feed.setTitle(node.selectSingleNode("title") != null ? node.selectSingleNode("title").getText() : "");
                    feed.setLink(node.selectSingleNode("link") != null ? node.selectSingleNode("link").getText() : "");
                    feed.setDescription(node.selectSingleNode("description") != null ? node.selectSingleNode("description").getText() : "");
                    feed.setPubDate(node.selectSingleNode("pubDate") != null ? node.selectSingleNode("pubDate").getText() : "");

                    feeds.add(feed);
                }
            }
        } catch (DocumentException ex) {
            LOG.warn(ex.getMessage(), ex);
            System.out.println(ansi().fgBright(RED).a("Error while reading feed.").reset());
        }
        return feeds;
    }
}
