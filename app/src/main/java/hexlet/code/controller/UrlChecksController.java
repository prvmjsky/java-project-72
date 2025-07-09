package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;

import java.sql.SQLException;

public class UrlChecksController {
    public static void create(Context ctx) throws SQLException {

        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId).orElseThrow(NotFoundResponse::new);
        var status = Unirest.get(url.getName())
            .asString()
            .getStatus();
        var check = new UrlCheck(status, urlId);

        UrlCheckRepository.save(check);
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
