package com.bds.BloodDonations;

public record ConfirmDonationRequest(
        Long adminId,
        Integer units
) {
}
