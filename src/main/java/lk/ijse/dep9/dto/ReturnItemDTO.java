package lk.ijse.dep9.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data /* toString(), Getters, Setters, equals(), hashCode() */
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemDTO implements Serializable {
    @NotNull(message = "Invalid issue note id")
    private Integer issueNoteId;
    @NotBlank(message = "ISBN can't be empty")
    @Pattern(regexp = "[0-9][0-9\\\\-]*[0-9]", message = "Invalid ISBN")
    private String isbn;
}
