package com.bds.BloodDonations;

public record ConfirmDonationRequest(
        Long donationId,
        Long adminId,
        Integer units
) {
}
