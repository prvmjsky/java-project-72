@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.MainPage

@param MainPage page

@template.layout.page(
    page = page,
    headTitle = "Главная страница",
    content = @`
        <h1>Привет, ${page.getCurrentUser()}!</h1>
        <h3>Введите ссылку на сайт</h3>
        <form>
            <input type="url" name="url" value="${page.getUrl()}">
            <input type="submit" value="Проверить">
        </form>
    `
)
