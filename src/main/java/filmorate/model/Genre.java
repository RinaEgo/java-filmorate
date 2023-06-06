package filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode
public class Genre {
    @Min(1)
    private final int id;
    @NotBlank
    @EqualsAndHashCode.Exclude
    private final String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}