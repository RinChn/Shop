package marketplace.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank(message = "The email cannot be empty")
    String email;
}
