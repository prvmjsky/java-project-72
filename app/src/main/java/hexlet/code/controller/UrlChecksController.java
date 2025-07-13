package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.SQLException;

public final class UrlChecksController {

    private UrlChecksController() {
        throw new AssertionError("Util class cannot be instantiated");
    }

    public static void create(Context ctx) throws SQLException {

        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId).orElseThrow(NotFoundResponse::new);

        HttpResponse<String> response = Unirest.get(url.getName()).asString();
        var status = response.getStatus();

        Document body = Jsoup.parse(response.getBody());
        var title = body.title();

        Element h1Element = body.selectFirst("h1");
        var h1 = h1Element != null ? h1Element.text() : null;

        Element descriptionElement = body.selectFirst("meta[name=description]");
        var description = descriptionElement != null ? descriptionElement.attr("content") : null;

        var check = new UrlCheck(status, title, h1, description, urlId);
        UrlCheckRepository.save(check);

        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.sessionAttribute("flash-type", "alert alert-success");
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
