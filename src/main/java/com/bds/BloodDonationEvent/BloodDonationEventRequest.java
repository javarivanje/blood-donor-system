package com.bds.BloodDonationEvent;

import com.bds.user.BloodType;
import com.bds.user.Users;

import java.time.LocalDate;

public record BloodDonationEventRequest(
        String eventName,
        LocalDate eventDate,
        BloodType bloodType,
        Integer units,
        Users users
) {
}
