package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePage {
    private String flash;
    private String flashType;
}
