package hi.im.jenga.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.html.HTMLElement;

import java.io.File;
import java.io.IOException;

public class HTMLManager {

    private Document doc = null;

    public final String injectTextById(String id, String text, File htmlFile) {

        try {
            doc = Jsoup.parse(htmlFile, "UTF-8");
            doc.getElementById(id).text(text);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc.text();

    }

    public final String injectTextById(String id, String text, String htmlText) {

            doc = Jsoup.parse(htmlText);
            doc.getElementById(id).text(text);

        return doc.text();

    }

    public final String getHtmlTextFromFile(File htmlFile) {

        try {
            doc = Jsoup.parse(htmlFile, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc.text();

    }

}
