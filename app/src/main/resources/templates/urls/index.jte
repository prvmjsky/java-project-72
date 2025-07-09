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
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Адрес</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(var url : page.getUrls())
                <tr>
                    <td>${url.getId()}</td>
                    <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                    @if (url.getChecks() != null)
                        <td>${String.valueOf(url.getChecks().getLast().getCreatedAt())}</td>
                        <td>${url.getChecks().getLast().getStatusCode()}</td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    @endif
`
)