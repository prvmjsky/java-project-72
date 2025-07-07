package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, url.getName());
            stmt.setTimestamp(2, url.getCreatedAt());
            stmt.executeUpdate();

            var keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                url.setId(keys.getLong(1));
            } else {
                throw new SQLException("DB has not returned an id after saving the entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                var name = rs.getString("name");
                var createdAt = rs.getTimestamp("created_at");
                var url = new Url(id, name, createdAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM users WHERE name = ?";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, name);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                var id = rs.getLong("id");
                var createdAt = rs.getTimestamp("created_at");
                var url = new Url(id, name, createdAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM users";
        try (
            var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            var rs = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (rs.next()) {
                var id = rs.getLong("id");
                var name = rs.getString("name");
                var createdAt = rs.getTimestamp("created_at");
                var url = new Url(id, name, createdAt);
                url.setId(id);
                result.add(url);
            }
            return result;
        }
    }
}
