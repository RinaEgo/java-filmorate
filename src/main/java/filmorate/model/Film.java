package filmorate.model;

import java.time.LocalDate;
import java.util.*;

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
    private String name;

    @Size(max = 200, message = "Длина описания превышает допустимое значение.")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;

    private Mpa mpa;

    private Set<Genre> genres;

    @JsonIgnore
    private Set<Integer> likes;


    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = new LinkedHashSet<>();
        this.likes = new LinkedHashSet<>();
    }


    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());

        return values;
    }
}
