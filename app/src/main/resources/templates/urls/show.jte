@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter
@param UrlPage page

@template.layout.page(
page = page,
headTitle = "Проверьте сайт",
content = @`
    <div class="col-lg-8 mx-auto p-4 py-md-5">
        <h2>Информация о сайте</h2>
        <table>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Адрес</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(page.getUrl().getCreatedAt())}</td>
            </tr>
        </table>
        <h2>Проверки</h2>
        <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post">
            <input type="submit" value="Запустить проверку">
        </form>
        @if (page.getChecks() != null)
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Код ответа</th>
                    <th>&lt;title&gt;</th>
                    <th>&lt;h1&gt;</th>
                    <th>&lt;description&gt;</th>
                    <th>Дата проверки</th>
                </tr>
                </thead>
                <tbody>
                @for (var check : page.getChecks())
                    <tr>
                        <th>${check.getId()}</th>
                        <th>${check.getStatusCode()}</th>
                        <th>${check.getTitle()}</th>
                        <th>${check.getH1()}</th>
                        <th>${check.getDescription()}</th>
                        <th>${DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(check.getCreatedAt())}</th>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`
)