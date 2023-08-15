package com.bds.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UsersRegistrationRequest(

        @NotEmpty(message = "First name may not be empty")
        @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters long ")
        String firstName,
        @NotEmpty(message = "Last name may not be empty")
        @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters long ")
        String lastName,
        @Email
        String email,
        Role role,
        BloodType bloodType
) {
}
