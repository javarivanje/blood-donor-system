package com.bds.BloodDonations;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
public class BloodDonationsController {

    private final BloodDonationsService bloodDonationsService;

    public BloodDonationsController(BloodDonationsService bloodDonationsService) {
        this.bloodDonationsService = bloodDonationsService;
    }

    @GetMapping("/available_blood_units")
    public List<BloodUnits> countTotalUnitsByBloodType() {
        return bloodDonationsService.countTotalUnitsByBloodType();
    }

    @PostMapping("/enter_donation")
    public void addBloodDonation(@RequestBody BloodDonationRequest bloodDonationRequest) {
        bloodDonationsService.addBloodDonation(bloodDonationRequest);
    }

    @PutMapping("/confirm_blood_donation/{donationId}")
    public void confirmBloodDonation(
            @PathVariable("donationId") Long donationId,
            @RequestBody ConfirmDonationRequest confirmDonationRequest) {
            bloodDonationsService.confirmBloodDonation(confirmDonationRequest);
    }

}
