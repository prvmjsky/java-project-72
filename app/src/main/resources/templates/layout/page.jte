@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param String headTitle
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="ru">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>${headTitle}</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q" crossorigin="anonymous"></script>
    </head>
    <body>
        @if(page != null && page.getFlash() != null)
            <div class="${page.getFlashType()}" role="alert">
                <p>${page.getFlash()}</p>
            </div>
        @endif
        <header>
            <nav>
                <h4><a href="${NamedRoutes.rootPath()}">На главную</a></h4>
                <h4><a href="${NamedRoutes.urlsPath()}">Сайты</a></h4>
            </nav>
        </header>
        ${content}
    </body>
</html>