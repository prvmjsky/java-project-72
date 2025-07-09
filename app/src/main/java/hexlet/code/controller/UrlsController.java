package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var latestChecks = new HashMap<Long, UrlCheck>();

        for (var check : UrlCheckRepository.getEntities()) {
            latestChecks.put(check.getUrlId(), check);
        }

        var page = new UrlsPage(urls, latestChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        var checks = UrlCheckRepository.getEntities().stream()
            .filter(check -> check.getUrlId().equals(id))
            .toList();

        var page = new UrlPage(url, checks);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException, URISyntaxException {

        try {
            var url = new URI(ctx.formParam("url")).normalize().toURL();
            var protocol = url.getProtocol();
            var authority = url.getAuthority();
            var urlName = String.format("%s://%s", protocol, authority);

            if (UrlRepository.findByName(urlName).isPresent()) {
                throw new IllegalArgumentException("Страница уже существует");
            }

            UrlRepository.save(new Url(urlName));

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "alert alert-success");
            ctx.redirect(NamedRoutes.rootPath());

        } catch (MalformedURLException | IllegalArgumentException e) {

            if (e instanceof MalformedURLException) {
                ctx.sessionAttribute("flash", "Некорректный URL");
            } else {
                ctx.sessionAttribute("flash", e.getMessage());
            }

            ctx.sessionAttribute("flash-type", "alert alert-danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }
}
