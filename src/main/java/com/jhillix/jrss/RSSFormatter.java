package com.jhillix.jrss;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * Formats the feed for console output.
 *
 * @author jhillix
 */
public class RSSFormatter {

    private static Logger LOG = Logger.getLogger(RSSFormatter.class);

    public RSSFormatter() {}

    /**
     * Takes a List composed of RSSFeed objects and passes it to output.vm for output.
     *
     * @param feeds list to output
     * @return the formatted output
     */
    public String format(final List<RSSFeed> feeds) {
        LOG.info("Formatting output.");
        System.out.println(ansi().fgBright(GREEN).a("Formatting output.").reset());

        if (feeds.isEmpty()) {
            return "Nothing to format! Feed is empty.";
        }

        Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("output.vm"));

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("feeds", feeds);
        StringWriter stringWriter = new StringWriter();

        try {
            Velocity.evaluate(velocityContext, stringWriter, "", reader);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            System.out.println(ansi().fgBright(RED).a("Oops! Something bad happened.").reset());
        }
        return stringWriter.toString();
    }
}
