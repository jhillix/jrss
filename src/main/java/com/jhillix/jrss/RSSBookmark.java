package com.jhillix.jrss;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * Bookmark an RSS <item> by using the <title> attribute. This class is also responsible for showing and removing bookmarks.
 *
 * @author jhillix
 */
public class RSSBookmark {

    private static Logger LOG = Logger.getLogger(RSSBookmark.class);

    public static final String OK = "Bookmark successfully added!";

    public static final String NG = "You must enter a valid title!";

    private static final String INVALID_BOOKMARK = "Could not locate a bookmark with that title.";

    private Preferences preferences;

    private String lineSeperator = System.lineSeparator();

    public RSSBookmark() {
        // Use this class as a node in which the preferences can be stored.
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    /**
     * Keeping it simple for now and only allowing one bookmark addition at a time. Future improvement is to allow user
     * to specify multiple titles at one time. Probably could be implemented better, but hey baby steps ;)
     *
     * @param  title that the user entered
     * @param  feeds list of RSSFeed objects that was parsed earlier
     * @return an OK or NG response to notify user
     */
    public String addBookmark(final String title, final List<RSSFeed> feeds) {
        RSSFeed bookmark = new RSSFeed();

        // Assume the worst.
        String response = NG;

        // Iterate through the List, verify that the title the user entered is a valid title. If we have a valid title,
        // update the response and grab the RSSFeed object associated with the valid title. NOTE: for now we only allow one
        // bookmark at a time. This will obviously need to be refactored when more than one bookmark at a time can be entered.
        for (RSSFeed feed : feeds) {
            if (feed.getTitle().toLowerCase().equals(title.toLowerCase())) {
                bookmark = feed;
                response = OK;
                break;
            }
        }

        // Add the bookmark to user preferences.
        if (OK.equals(response)) {
            String realTitle = bookmark.getTitle();

            preferences.put(realTitle + "-title", realTitle);
            preferences.put(realTitle + "-link", bookmark.getLink());
            preferences.put(realTitle + "-pubDate", bookmark.getPubDate());
            preferences.put(realTitle + "-description", bookmark.getDescription());
        }

        return response;
    }

    /**
     * Shows a users saved bookmarks if they exist or a message if none exist.
     */
    public void showBookmarks() {
        if (hasBookmarks()) {
            System.out.println("Here are your current bookmarks:");
            try {
                for (String pref : preferences.keys()) {
                    // Only show the title.
                    if (pref.endsWith("-title")) {
                        System.out.println(ansi().fg(CYAN).a(pref.substring(0, pref.lastIndexOf("-title"))).reset());
                    }
                }
            } catch (BackingStoreException ex) {
                LOG.error(ex.getMessage(), ex);
                System.out.println(ansi().fgBright(RED).a("Oops! Something bad happened.").reset());
            }
        } else {
            System.out.println("No bookmarks have been added.");
        }
    }

    /**
     * Checks if this user has any bookmarks.
     *
     * @return true if bookmarks exist otherwise, false
     */
    public boolean hasBookmarks() {
        boolean bool = false;
        try {
            if (preferences != null && preferences.keys().length > 0) {
                bool = true;
            }
        } catch (BackingStoreException ex) {
            LOG.error(ex.getMessage(), ex);
            System.out.println(ansi().fgBright(RED).a("Oops! Something bad happened.").reset());
        }

        return bool;
    }

    /**
     * Gets a bookmark or bookmarks if there is more than one and displays them to the user. The user will see a list of
     * all bookmarks they currently have. The user can then copy & paste one of those values or all of them when prompted.
     *
     * @param bookmarks separated by a comma if there is more than one or just a single bookmark
     */
    public void getBookmark(final String bookmarks) {

        if (hasBookmarks()) {
            List<RSSFeed> feeds = new ArrayList<>();
            for (String bookmark : bookmarks.split(",")) {

                bookmark = bookmark.trim();

                // Is this bookmark valid?
                if (!isBookmarkValid(bookmark)) {
                    System.out.println(ansi().fgBright(RED).a(INVALID_BOOKMARK + " (" + bookmark + ")" + lineSeperator));
                    continue;
                }

                // Populate a RSSFeed object from the users bookmarks.
                RSSFeed feed = new RSSFeed();
                feed.setTitle(preferences.get(bookmark + "-title", ""));
                feed.setLink(preferences.get(bookmark + "-link", ""));
                feed.setPubDate(preferences.get(bookmark + "-pubDate", ""));
                feed.setDescription(preferences.get(bookmark + "-description", ""));

                // Add it to the List.
                feeds.add(feed);
            }

            // Format it.
            if (!feeds.isEmpty()) {
                System.out.println(new RSSFormatter().format(feeds));
                System.out.println("End of bookmarks." + lineSeperator);
            }
        }
    }

    /**
     * Checks if a bookmark is valid by checking its existence. Case matters!
     *
     * @param bookmark to check
     * @return true if we find a match otherwise false
     */
    public boolean isBookmarkValid(final String bookmark) {
        boolean bool = false;

        String test = preferences.get(bookmark.trim() + "-title", INVALID_BOOKMARK);
        if (!INVALID_BOOKMARK.equals(test)) {
            bool = true;
        }
        return bool;
    }

    /**
     * Removes a single bookmark if it exists.
     */
    public void removeBookmark(final String bookmark) {

        if (hasBookmarks() && isBookmarkValid(bookmark)) {
            preferences.remove(bookmark + "-title");
            preferences.remove(bookmark + "-link");
            preferences.remove(bookmark + "-pubDate");
            preferences.remove(bookmark + "-description");

            System.out.println(ansi().fgBright(GREEN).a("Successfully removed " + "\"" + bookmark + "\""));
        } else {
            System.out.println("Either you have no bookmarks or the bookmark you entered is incorrect. Case matters.");
        }
    }

    /**
     * Removes all bookmarks for this user.
     */
    public void removeAllBookmarks() {

        if (hasBookmarks()) {

            try {
                preferences.clear();
            } catch (BackingStoreException ex) {
                LOG.error(ex.getMessage(), ex);
                System.out.println(ansi().fgBright(RED).a("Oops! Something bad happened.").reset());
            }

            System.out.println(ansi().fgBright(GREEN).a("Successfully removed all bookmarks!"));
        } else {
            System.out.println("You have no bookmarks!");
        }
    }
}
