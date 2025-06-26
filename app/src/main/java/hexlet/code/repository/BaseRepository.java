package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseRepository {
    public static HikariDataSource dataSource;
}
