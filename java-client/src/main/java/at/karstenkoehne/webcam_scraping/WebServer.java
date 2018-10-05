package at.karstenkoehne.webcam_scraping;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
    private HttpServer httpServer;

    public void init() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.createContext("/video", new VideoHandler());
        httpServer.createContext("/videosrc", new SourceHandler());
        httpServer.setExecutor(null); // creates a default executor
        httpServer.start();
    }

    private static class VideoHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = String.format(
                    "<video controls autoplay><source src=\"%s\" type=\"video/mp4\"></video> ", scrapVideoSource());
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/html");
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class SourceHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = scrapVideoSource();
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/plain");
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static String scrapVideoSource() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5131&design=v3&c0=0&c2=1&lg=en&s=0&lc=5132")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(doc.select("video#fer_video source[src]").first().attr("src"));
        return doc.select("video#fer_video source[src]").first().attr("src");
    }
}
