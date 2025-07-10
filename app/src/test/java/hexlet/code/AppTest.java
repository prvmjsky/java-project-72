package hexlet.code;

import hexlet.code.controller.UrlsController;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import okhttp3.Headers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AppTest {
    private Context ctx;
    private static MockWebServer server;
    private String rawUrl;
    private Javalin app;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        ctx = mock(Context.class);

        server = new MockWebServer();
        var response = new MockResponse(200, new Headers.Builder().build(), "i'm so tired");
        server.enqueue(response);
        server.start();

        rawUrl = server.url("/").toString();

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
    public void testInvalidUrl() {
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
    public void testPostUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + rawUrl;
            var postResponse = client.post(NamedRoutes.urlsPath(), requestBody);
            assertEquals(200, postResponse.code());
            assertTrue(postResponse.body().string().contains("Главная страница"));

            var urlName = UrlsController.normalizeUrlName(rawUrl);
            var getResponse = client.get(NamedRoutes.urlsPath());
            assertTrue(getResponse.body().string().contains(urlName));
        });
    }

    @Test
    public void testPostUrlCheck() {
        JavalinTest.test(app, (server, client) -> {
            var urlName = UrlsController.normalizeUrlName(rawUrl);
            var url = new Url(urlName);
            UrlRepository.save(url);
            var urlId = UrlRepository.findByName(urlName).get().getId();

            var postResponse = client.post(NamedRoutes.urlChecksPath(urlId));
            assertEquals(200, postResponse.code());
        });
    }
}
