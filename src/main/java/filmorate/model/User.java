package filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Integer id;

    @Email(message = "Email введён некорректно.")
    @NotNull(message = "Email не должен быть пуст.")
    @NotBlank(message = "Email введён некорректно.")
    private final String email;

    @NotNull(message = "Логин не должен быть пуст.")
    @NotBlank(message = "Логин введён некорректно.")
    private final String login;

    private String name;

    @NotNull(message = "Дата рождения должна быть заполнена.")
    @Past(message = "Дата рождения не может быть в будущем.")
    private final LocalDate birthday;
}
