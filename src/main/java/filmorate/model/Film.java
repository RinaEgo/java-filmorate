package filmorate.model;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Film {

    private Integer id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Integer duration;
}
