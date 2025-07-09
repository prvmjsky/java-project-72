package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks (status_code, url_id, created_at) VALUES (?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, check.getStatusCode());
            stmt.setLong(2, check.getUrlId());
            stmt.setTimestamp(3, check.getCreatedAt());
            stmt.executeUpdate();

            var keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                check.setId(keys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id after saving the entity");
            }
        }
    }

    public static Optional<UrlCheck> find(Long id) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE id = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                var check = UrlCheck.builder()
                    .id(id)
                    .statusCode(rs.getInt("status_code"))
                    .urlId(rs.getLong("url_id"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .build();
                return Optional.of(check);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<UrlCheck> getChecksByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql);
        ) {
            stmt.setLong(1, urlId);

            var rs = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (rs.next()) {
                var check = UrlCheck.builder()
                    .id(rs.getLong("id"))
                    .statusCode(rs.getInt("status_code"))
                    .urlId(urlId)
                    .createdAt(rs.getTimestamp("created_at"))
                    .build();
                result.add(check);
            }

            return result;
        }
    }
}
