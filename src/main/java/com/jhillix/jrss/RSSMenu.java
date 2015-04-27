package com.jhillix.jrss;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: jhillix
 * Date: 4/24/15
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class RSSMenu {

    private static Logger LOG = Logger.getLogger(RSSMenu.class);

    public RSSMenu() {}

    /**
     * Show the main menu.
     *
     * @return the menu as a String
     */
    public String showMenu() {
        Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("menu.vm"));

        VelocityContext velocityContext = new VelocityContext();
        StringWriter stringWriter = new StringWriter();

        try {
            Velocity.evaluate(velocityContext, stringWriter, "", reader);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return stringWriter.toString();
    }
}
