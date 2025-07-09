package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import okhttp3.Headers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AppTest {
    private static MockWebServer server;
    private Javalin app;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        server = new MockWebServer();
        var response = new MockResponse(200, new Headers.Builder().build(), "i'm so tired");
        server.enqueue(response);
        server.start();

        app = App.getApp();
    }

    @AfterAll
    public static void tearDown() {
        server.close();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertEquals(200, response.code());
            assertTrue((response.body().string()).contains("Главная страница"));
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertEquals(200, response.code());
            assertTrue((response.body().string()).contains("Добавленные сайты"));
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {

            var url1 = new Url("https://one.com");
            var url2 = new Url("https://two.com:7070");

            UrlRepository.save(url1);
            UrlRepository.save(url2);

            var response1 = client.get(NamedRoutes.urlPath(url1.getId()));
            var response2 = client.get(NamedRoutes.urlPath(url2.getId()));

            assertEquals(200, response1.code());
            assertTrue((response1.body().string()).contains("Сайт"));

            assertEquals(200, response2.code());
            assertTrue((response2.body().string()).contains("Сайт"));
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("1"));
            assertEquals(404, response.code());
        });
    }

    @Test
    public void testSaveUrl() {
        var urlName = "https://one.com";

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + urlName;
            var postResponse = client.post(NamedRoutes.urlsPath(), requestBody);
            var url = UrlRepository.findByName(urlName).orElseThrow(NotFoundResponse::new);

            assertEquals(urlName, url.getName());
            assertEquals(200, postResponse.code());
            assertTrue(postResponse.body().string().contains("Главная страница"));

            var getResponse = client.get(NamedRoutes.urlsPath());
            assertTrue(getResponse.body().string().contains(urlName));
        });
    }

    @Test
    public void testUrlCheck() throws SQLException {
        var urlName = server.url("/").toString();
        var url = new Url(urlName);
        UrlRepository.save(url);
        var urlId = UrlRepository.findByName(urlName).get().getId();

        JavalinTest.test(app, (server, client) -> {
            var postResponse = client.post(NamedRoutes.urlChecksPath(urlId));
            assertEquals(200, postResponse.code());
        });
    }
}
