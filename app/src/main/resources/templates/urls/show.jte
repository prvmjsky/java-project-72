@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
    page = page,
    headTitle = "Сайт",
    content = @`
        <div class="col-lg-8 mx-auto p-4 py-md-5">
            <main>
                <h1>${page.getUrl().getName()}</h1>
            </main>
        </div>
    `
)