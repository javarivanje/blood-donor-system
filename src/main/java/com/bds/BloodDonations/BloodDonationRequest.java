package com.bds.BloodDonations;

import com.bds.user.Users;

import java.time.LocalDate;

public record BloodDonationRequest(
        Integer units,
        LocalDate donationDate,
        Users donor,
        Users admin
) {
}
