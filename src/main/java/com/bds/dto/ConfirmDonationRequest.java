package com.bds.dto;

public record ConfirmDonationRequest(
        Long adminId,
        Integer units
) {
}
