package filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@EqualsAndHashCode
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

    @JsonIgnore
    private Set<Integer> likes;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }
}
