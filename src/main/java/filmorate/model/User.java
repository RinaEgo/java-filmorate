package filmorate.model;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Integer id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
