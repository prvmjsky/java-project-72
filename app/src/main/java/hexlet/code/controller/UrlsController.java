package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("users/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        var page = new UrlPage(url);
        ctx.render("users/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {

        try {
            var url = ctx.formParamAsClass("url", URI.class).get().normalize().toURL();
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
            var page = new MainPage(
                ctx.sessionAttribute("currentUser"),
                ctx.formParam("url")
            );

            if (e instanceof MalformedURLException) {
                page.setFlash("Некорректный URL");
            } else {
                page.setFlash(e.getMessage());
            }

            page.setFlashType("alert alert-danger");
            ctx.render(NamedRoutes.rootPath(), model("page", page));
        }
    }
}
