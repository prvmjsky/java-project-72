@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param String headTitle
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>${headTitle}</title>

        <link rel="stylesheet" href="../../bootstrap/css/bootstrap-grid.min.css">
        <link rel="stylesheet" href="../../bootstrap/css/bootstrap-utilities.min.css">
    </head>
    <body>
        @if(page != null && page.getFlash() != null)
            <div class="${page.getFlashType()}" role="alert">
                <p>${page.getFlash()}</p>
            </div>
        @endif
        <header>
            <nav>
                <h1><a href="${NamedRoutes.rootPath()}">На главную</a></h1>
            </nav>
        </header>

        @if(page != null && page.getFlash() != null)
            <p>${page.getFlash()}</p>
        @endif

        ${content}
    </body>
</html>