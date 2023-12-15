package com.bds.dto;

import com.bds.models.Users;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record BloodDonationRequest(
        @Min(value = 1, message = "invalid units amount: Must be greater than 0")
        Integer units,
        @FutureOrPresent(message = "Invalid date: Enter a date in format \"YYYY-MM-DD\" +" +
                " and date must not be before today")
        LocalDate donationDate,
        Users donor,
        Users admin
) {
}
