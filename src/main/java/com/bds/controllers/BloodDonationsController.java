package com.bds.controllers;

import com.bds.dto.BloodDonationRequest;
import com.bds.dto.BloodUnits;
import com.bds.dto.ConfirmDonationRequest;
import com.bds.dto.InitiateBloodDonationRequest;
import com.bds.models.BloodDonations;
import com.bds.services.BloodDonationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class BloodDonationsController {

    private final BloodDonationsService bloodDonationsService;

    public BloodDonationsController(BloodDonationsService bloodDonationsService) {
        this.bloodDonationsService = bloodDonationsService;
    }

    @GetMapping("/admin/available_blood_units")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodUnits>> countAvailableUnitsByBloodType() {

        return new ResponseEntity<>(bloodDonationsService.countAvailableUnitsByBloodType(),
                HttpStatus.OK);
    }

    @PostMapping("/admin/enter_donation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodDonations> addBloodDonation(
            @RequestBody BloodDonationRequest bloodDonationRequest) {

        return new ResponseEntity<>(
                bloodDonationsService.addBloodDonation(bloodDonationRequest),
                HttpStatus.CREATED);
    }

    @PatchMapping("/admin/confirm_blood_donation/{donationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void confirmBloodDonation(
            @PathVariable("donationId") Long donationId,
            @RequestBody ConfirmDonationRequest confirmDonationRequest) {
        bloodDonationsService.confirmBloodDonation(donationId, confirmDonationRequest);
    }

    @PostMapping("/donor/initiate_blood_donation")
    @PreAuthorize("hasRole('DONOR')")
    public void initiateBloodDonation(InitiateBloodDonationRequest initiateBloodDonationRequest) {
        bloodDonationsService.initiateBloodDonation(initiateBloodDonationRequest);
    }

    @GetMapping("/donor/my_blood_donations/{donorId}")
    @PreAuthorize("hasRole('DONOR')")
    public List<BloodDonations> getDonorBloodDonations(
            @PathVariable("donorId") Long donorId) {
        return bloodDonationsService.getBloodDonations(donorId);
    }

}
