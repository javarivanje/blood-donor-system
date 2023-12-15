package com.bds.dto;

import com.bds.models.Users;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BloodDonationEventRequest(
        @NotEmpty(message = "Invalid event name: Empty event name")
        @Size(min = 2, max = 32, message = "Invalid event name: Must be between 2 and 32 characters long ")
        String eventName,
        @FutureOrPresent(message = "Invalid date: Enter a date in format \"YYYY-MM-DD\" +" +
                " and date must not be before today")
        LocalDate eventDate,
        @Pattern(regexp = "APos|ANeg|BPos|BNeg|ABPos|ABNeg|OPos|ONeg",
                message = "Invalid blood type: " +
                        "Must start with capital letter for group followed by Pos or Neg Ex: APos, ANeg...")
        String bloodType,
        @Min(value = 1, message = "Invalid units amount: Must be between 1 and 5")
        @Max(value = 5, message = "Invalid units amount: Must be between 1 and 5")
        Integer units,
        Users users
) {
}
