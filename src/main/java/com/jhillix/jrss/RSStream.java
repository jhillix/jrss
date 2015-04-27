package com.jhillix.jrss;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * Responsible for handling the URL entered by the user.
 *
 * @author jhillix
 */
public class RSStream {

    private static Logger LOG = Logger.getLogger(RSStream.class);

    private static final String YOU_CHOSE_POORLY = "http://www.qwantz.com/rssfeed.php";

    private URL url;

    public RSStream() {}

    /**
     * Attempts to open the RSS URL provided by the user. If the user did not provide a URL then will fallback to a default.
     * If an error occurs during processing of the user entered URL then null will be returned.
     *
     * @return InputStream of the RSS feed or null if an error occurs
     * @throws java.io.IOException
     */
    public InputStream stream(final String stream) throws IOException {
        try {
            if (StringUtils.isBlank(stream)) {
                LOG.info("Opening default:" + YOU_CHOSE_POORLY);
                System.out.println(ansi().fgBright(GREEN).a("Opening default:" + YOU_CHOSE_POORLY).reset());
                url = new URL(YOU_CHOSE_POORLY);
            } else {
                LOG.info("Opening " + stream);
                System.out.println(ansi().fgBright(GREEN).a("Opening: " + stream).reset());
                url = new URL(stream);
            }
        } catch (MalformedURLException ex) {
            LOG.error(ex.getMessage(), ex);
            System.out.println(ansi().fgBright(RED).a("Invalid URL!").reset());
        }
        return url != null ? url.openStream() : null;
    }
}
