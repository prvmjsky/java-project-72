@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@import java.time.format.DateTimeFormatter
@param UrlsPage page

@template.layout.page(
page = page,
headTitle = "Добавленные сайты",
content = @`
    @if(page.getUrls().isEmpty())
        <h2>Ещё не добавлено ни одного сайта ( ͡° ʖ̯ ͡° )</h2>
    @else
        <h1>Добавленные сайты</h1>
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
                    @if (page.getLatestChecks() != null && page.getLatestChecks().containsKey(url.getId()))
                        <td>${DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(page.getLatestChecks().get(url.getId()).getCreatedAt())}</td>
                        <td>${page.getLatestChecks().get(url.getId()).getStatusCode()}</td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    @endif
`
)