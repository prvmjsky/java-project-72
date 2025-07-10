@import hexlet.code.dto.MainPage
@import hexlet.code.util.NamedRoutes

@param MainPage page

@template.layout.page(
    page = page,
    headTitle = "Главная страница",
    content = @`
        <h1>Привет, ${page.getCurrentUser()}!</h1>
        <h4>Введите ссылку на сайт</h4>
        <form action ="${NamedRoutes.urlsPath()}" method="post">
            <input type="url" name="url" value="${page.getUrl()}" required>
            <input type="submit" value="Проверить">
        </form>
    `
)
