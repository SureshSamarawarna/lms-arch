package lk.ijse.dep9.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueNoteDTO {
    @Null(message = "Issue note id can't have a value")
    private Integer id;
    @NotNull(message = "Date can't be empty")
    private LocalDate date;
    @NotBlank(message = "Member ID can't be empty")
    @Pattern(regexp = "[A-Fa-f0-9]{8}(-[A-Fa-f0-9]{4}){3}-[A-Fa-f0-9]{12}", message = "Invalid member id")
    private String memberId;
    @NotEmpty(message = "Books can't be empty")
    private ArrayList<
            @NotBlank(message = "isbn can't be a null value")
            @Pattern(regexp = "[0-9][0-9\\\\-]*[0-9]", message = "Invalid isbn")
                    String> books = new ArrayList<>();
}
