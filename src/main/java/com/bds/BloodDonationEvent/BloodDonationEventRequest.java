package com.bds.BloodDonationEvent;

import com.bds.user.BloodType;
import com.bds.user.Users;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record BloodDonationEventRequest(
        @NotEmpty
        String eventName,
        
        LocalDate eventDate,
        BloodType bloodType,
        Integer units,
        Users users
) {
}
