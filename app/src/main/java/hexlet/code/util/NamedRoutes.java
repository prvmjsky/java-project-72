package hexlet.code.util;

public class NamedRoutes {

    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls/";
    }
    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }
    public static String urlPath(String id) {
        return urlsPath() + id;
    }

    public static String urlChecksPath(Long id) {
        return urlPath(id) + "/checks";
    }
    public static String urlChecksPath(String id) {
        return urlPath(id) + "/checks";
    }
}
