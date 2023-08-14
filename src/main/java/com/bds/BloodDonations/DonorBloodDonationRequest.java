package com.bds.BloodDonations;

import com.bds.user.Users;

import java.time.LocalDate;

public record DonorBloodDonationRequest(
        Users donor,
        Integer units,
        LocalDate donationDate
) {
}
