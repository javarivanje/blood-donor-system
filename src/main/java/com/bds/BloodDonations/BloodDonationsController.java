package com.bds.BloodDonations;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class BloodDonationsController {

    private final BloodDonationsService bloodDonationsService;

    public BloodDonationsController(BloodDonationsService bloodDonationsService) {
        this.bloodDonationsService = bloodDonationsService;
    }

    @GetMapping("/admin/available_blood_units")
    public List<BloodUnits> countTotalUnitsByBloodType() {
        return bloodDonationsService.countTotalUnitsByBloodType();
    }

    @PostMapping("admin/enter_donation")
    public void addBloodDonation(@RequestBody BloodDonationRequest bloodDonationRequest) {
        bloodDonationsService.addBloodDonation(bloodDonationRequest);
    }

    @PatchMapping("/admin/confirm_blood_donation/{donationId}")
    public void confirmBloodDonation(
            @PathVariable("donationId") Long donationId,
            @RequestBody ConfirmDonationRequest confirmDonationRequest) {
            bloodDonationsService.confirmBloodDonation(donationId, confirmDonationRequest);
    }

    @PostMapping("/donor/initiate_blood_donation")
    public void initiateBloodDonation(InitiateBloodDonationRequest initiateBloodDonationRequest) {
        bloodDonationsService.initiateBloodDonation(initiateBloodDonationRequest);
    }

    @GetMapping("/donor/my_blood_donations")
    public List<BloodDonations> getDonorBloodDonations(Long donorId) {
        return bloodDonationsService.getBloodDonations(donorId);
    }

}
