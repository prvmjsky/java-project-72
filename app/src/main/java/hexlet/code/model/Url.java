package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Url {
    private Long id;
    @NonNull private String name;
    @NonNull private Timestamp createdAt;
}
