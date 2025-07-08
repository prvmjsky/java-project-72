@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
    page = page,
    headTitle = "Добавленные сайты",
    content = @`
        <h1>Добавленные сайты</h1>
        @if(page.getUrls().isEmpty())
            <p>Ещё не добавлено ни одного сайта</p>
        @else
            @for(var url : page.getUrls())
                <div>
                    <h3><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></h3>
                </div>
            @endfor
        @endif
    `
)