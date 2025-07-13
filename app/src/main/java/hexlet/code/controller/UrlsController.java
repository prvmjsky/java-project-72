package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class UrlsController {

    private UrlsController() {
        throw new AssertionError("Util class cannot be instantiated");
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var latestChecks = UrlCheckRepository.getLatestChecks();

        var page = new UrlsPage(urls, latestChecks);

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        var checks = UrlCheckRepository.findByUrlId(id);

        var page = new UrlPage(url, checks);

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {

        var inputUrl = ctx.formParam("url");
        URI parsedUrl;
        try {
            parsedUrl = new URI(inputUrl);
        } catch (URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "alert alert-danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String urlName;
        try {
            urlName = normalizeUrlName(parsedUrl);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "alert alert-danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        if (UrlRepository.findByName(urlName).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "alert alert-danger");
            ctx.redirect(NamedRoutes.rootPath());
        } else {
            UrlRepository.save(new Url(urlName));
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "alert alert-success");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static String normalizeUrlName(URI rawUrl) throws MalformedURLException {
        var url = rawUrl.normalize().toURL();
        var protocol = url.getProtocol();
        var authority = url.getAuthority();
        return String.format("%s://%s", protocol, authority);
    }
}
