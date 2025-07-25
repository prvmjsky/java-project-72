package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UrlCheckRepository extends BaseRepository {

    private UrlCheckRepository() {
        throw new AssertionError("Util class cannot be instantiated");
    }

    public static void save(UrlCheck check) throws SQLException {
        var sql = """
            INSERT INTO url_checks
                (status_code, title, h1, description, url_id, created_at)
            VALUES
                (?, ?, ?, ?, ?, ?)
            """;
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, check.getTitle());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getDescription());
            stmt.setLong(5, check.getUrlId());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();

            var keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                check.setId(keys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id after saving the entity");
            }
        }
    }

    public static List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, urlId);
            var rs = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (rs.next()) {
                var check = UrlCheck.builder()
                    .id(rs.getLong("id"))
                    .statusCode(rs.getInt("status_code"))
                    .title(rs.getString("title"))
                    .h1(rs.getString("h1"))
                    .description(rs.getString("description"))
                    .urlId(urlId)
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
                result.add(check);
            }

            return result;
        }
    }

    public static Map<Long, UrlCheck> getLatestChecks() throws SQLException {
        var sql = """
            SELECT DISTINCT ON (url_id) *
            FROM url_checks ORDER BY url_id DESC, id DESC
            """;
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            var rs = stmt.executeQuery();
            var result = new HashMap<Long, UrlCheck>();
            while (rs.next()) {
                var urlId = rs.getLong("url_id");
                var check = UrlCheck.builder()
                    .id(rs.getLong("id"))
                    .statusCode(rs.getInt("status_code"))
                    .title(rs.getString("title"))
                    .h1(rs.getString("h1"))
                    .description(rs.getString("description"))
                    .urlId(urlId)
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
                result.put(urlId, check);
            }

            return result;
        }
    }
}
