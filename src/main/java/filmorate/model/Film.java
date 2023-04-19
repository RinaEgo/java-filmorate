package filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Film {

    private Integer id;

    @NotNull(message = "Название не должно быть пустым.")
    @NotBlank(message = "Название введено некорректно.")
    private final String name;

    @Size(max = 200, message = "Длина описания превышает допустимое значение.")
    private final String description;

    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private final Integer duration;
}
