@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param String headTitle
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${headTitle}</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>

    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            padding: 20px;
        }

        header {
            margin-bottom: 20px;
        }

        nav h4 {
            display: inline-block;
            margin-right: 15px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #dee2e6;
            text-align: left;
        }

        th {
            background-color: #e9ecef;
            font-weight: 600;
        }

        h2 {
            margin-top: 30px;
            margin-bottom: 15px;
        }

        form input[type="url"],
        form input[type="text"],
        form input[type="submit"] {
            display: inline-block;
            margin-right: 10px;
            margin-top: 10px;
        }

        form input[type="url"],
        form input[type="text"] {
            padding: 8px 12px;
            border-radius: 4px;
            border: 1px solid;
        }

        form input[type="submit"] {
            padding: 8px 16px;
            border-radius: 4px;
            border: none;
            background-color: #0d6efd;
            color: white;
            cursor:pointer;
        }

        form input[type="submit"]:hover {
           background-color:#0b5ed7;
        }

        footer {
            font-weight: bold;
            position:absolute;
            bottom:0;
            right:0;
            background-color:#0d6efd;
            color:white;
            padding:8px 6px;
            font-size:14px;
        }
    </style>
</head>
<body>
@if(page != null && page.getFlash() != null)
    <div class="alert ${page.getFlashType()}" role="alert">
        <p>${page.getFlash()}</p>
    </div>
@endif
<header>
    <nav class="mb-4">
        <h4><a href="${NamedRoutes.rootPath()}" class="text-decoration-none text-primary">На главную</a></h4>
        <h4><a href="${NamedRoutes.urlsPath()}" class="text-decoration-none text-primary">Сайты</a></h4>
    </nav>
</header>
${content}
<footer>
    <h6>created by prvmjsky</h6>
</footer>
</body>
</html>