package lk.ijse.dep9.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDTO implements Serializable {
    @NotBlank(message = "Member id can't be empty")
    @Pattern(regexp = "([A-Fa-f0-9]{8}(-[A-Fa-f0-9]{4}){3}-[A-Fa-f0-9]{12})", message = "Invalid member id")
    private String memberId;
    @NotEmpty(message = "Return items can't be empty")
    private List<@NotNull(message = "Return item can't be null") @Valid ReturnItemDTO> returnItems = new ArrayList<>();
}
