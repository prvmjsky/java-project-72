package hexlet.code;

import hexlet.code.controller.UrlsController;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import okhttp3.Headers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class AppTest {
    private Javalin app;
    private Context ctx;
    private static MockWebServer mockWebServer;
    private String rawUrl;

    private static Document html;
    private static String title = "title example";
    private static String h1 = "h1 example";
    private static String description = "description example";

    @BeforeAll
    static void mockHtml() {
        title = "title example";
        h1 = "h1 example";
        description = "description example";

        html = Jsoup.parse("");
        html.title(title);
        var head = html.appendElement("html").appendElement("head");
        head.appendElement("meta")
            .attr("name", "description")
            .attr("content", description);

        var body = html.appendElement("body");
        body.appendElement("h1").text(h1);
    }

    @BeforeEach
    void setUp() throws SQLException, IOException {
        mockWebServer = new MockWebServer();
        var response = new MockResponse(200, new Headers.Builder().build(), html.toString());
        mockWebServer.enqueue(response);
        mockWebServer.start();

        rawUrl = mockWebServer.url("/").toString();

        app = App.getApp();
        ctx = mock(Context.class);
    }

    @AfterAll
    static void tearDown() {
        mockWebServer.close();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertEquals(200, response.code());
            assertTrue((response.body().string()).contains("Главная страница"));
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertEquals(200, response.code());
            assertTrue((response.body().string()).contains("Добавленные сайты"));
        });
    }

    @Test
    void testUrlPage() {
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
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("1"));
            assertEquals(404, response.code());
        });
    }

    @Test
    void testUrlDuplicate() {
        JavalinTest.test(app, (server, client) -> {
            when(ctx.formParam("url")).thenReturn(rawUrl);

            UrlsController.create(ctx);
            verify(ctx).sessionAttribute("flash", "Страница успешно добавлена");
            verify(ctx).sessionAttribute("flash-type", "alert alert-success");

            assertDoesNotThrow(() -> UrlsController.create(ctx));
            verify(ctx).sessionAttribute("flash", "Страница уже существует");
            verify(ctx).sessionAttribute("flash-type", "alert alert-danger");

            verify(ctx, times(2)).redirect(NamedRoutes.rootPath());
        });
    }

    @Test
    void testUrlIncorrect() {
        JavalinTest.test(app, (server, client) -> {
            when(ctx.formParam("url")).thenReturn(" " + rawUrl);
            assertDoesNotThrow(() -> UrlsController.create(ctx));
            verify(ctx).sessionAttribute("flash", "Некорректный URL");
            verify(ctx).sessionAttribute("flash-type", "alert alert-danger");
            verify(ctx).redirect(NamedRoutes.rootPath());
        });
    }

    @Test
    void testPostUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + rawUrl;
            try (var postResponse = client.post(NamedRoutes.urlsPath(), requestBody)) {
                assertEquals(200, postResponse.code());
                assertTrue(postResponse.body().string().contains("Главная страница"));
            }

            var parsedUrl = new URI(rawUrl);
            var urlName = UrlsController.normalizeUrlName(parsedUrl);
            var getResponse = client.get(NamedRoutes.urlsPath());
            assertTrue(getResponse.body().string().contains(urlName));
        });
    }

    @Test
    void testPostUrlCheck() {
        JavalinTest.test(app, (server, client) -> {
            var parsedUrl = new URI(rawUrl);
            var urlName = UrlsController.normalizeUrlName(parsedUrl);
            var url = new Url(urlName);
            UrlRepository.save(url);
            var urlId = UrlRepository.findByName(urlName).orElseThrow(NotFoundResponse::new).getId();

            try (var postResponse = client.post(NamedRoutes.urlChecksPath(urlId))) {
                assertEquals(200, postResponse.code());
            }

            var check = UrlCheckRepository.findByUrlId(urlId).getLast();
            assertEquals(200, check.getStatusCode());
            assertEquals(title, check.getTitle());
            assertEquals(h1, check.getH1());
            assertEquals(description, check.getDescription());
        });
    }
}
