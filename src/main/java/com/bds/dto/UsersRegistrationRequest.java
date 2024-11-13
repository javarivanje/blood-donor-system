package com.bds.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsersRegistrationRequest(

        @NotEmpty(message = "Invalid first name: Empty first name")
        @Size(min = 2, max = 32, message = "Invalid first name: Must be between 2 and 32 characters long ")
        String firstName,
        @NotEmpty(message = "Invalid last name: Empty last name")
        @Size(min = 2, max = 32, message = "Invalid last name: Must be between 2 and 32 characters long ")
        String lastName,
        @Email(message = "Invalid email : Entered email '${validatedValue}' must be existing email address")
        String email,
        @Pattern(regexp = "ADMIN|DONOR", message = "Invalid role: The validated role '${validatedValue}'" +
                " must be in proper format")
        String role,
        @Pattern(regexp = "APos|ANeg|BPos|BNeg|ABPos|ABNeg|OPos|ONeg",
                message = "Invalid blood type: " +
                        "Must start with capital letter for group followed by Pos or Neg Ex: APos, ANeg...")
        String bloodType
) {
}
