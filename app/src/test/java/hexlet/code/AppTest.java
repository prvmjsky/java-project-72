package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AppTest {
    private Javalin app;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        app = App.getApp();
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
            var url = UrlRepository.findByName(urlName);

            assertEquals(urlName, url.get().getName());
            assertEquals(200, postResponse.code());
            assertTrue(postResponse.body().string().contains("Главная страница"));

            var getResponse = client.get(NamedRoutes.urlsPath());
            assertTrue(getResponse.body().string().contains(urlName));
        });
    }
}
