@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.MainPage

@param MainPage page

@template.layout.page(
    headTitle = "Главная страница",
    content = @`
        <h1>Привет, ${page.getCurrentUser()}!</h1>
    `
)
