package com.bds.BloodDonations;

import com.bds.BloodDonationEvent.BloodDonationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BloodUnits>> countTotalUnitsByBloodType() {

        return new ResponseEntity<>(bloodDonationsService.countTotalUnitsByBloodType(),
                HttpStatus.OK);
    }

    @PostMapping("/admin/enter_donation")
    public ResponseEntity<BloodDonations> addBloodDonation(
            @RequestBody BloodDonationRequest bloodDonationRequest) {

        return new ResponseEntity<>(
                bloodDonationsService.addBloodDonation(bloodDonationRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/admin/confirm_blood_donation/{donationId}")
    public void confirmBloodDonation(
            @PathVariable("donationId") Long donationId,
            @RequestBody ConfirmDonationRequest confirmDonationRequest) {
            bloodDonationsService.confirmBloodDonation(confirmDonationRequest);
    }

    @PostMapping("/donor/blood_donation")
    public void addDonorBloodDonation(
            @RequestBody DonorBloodDonationRequest donorBloodDonationRequest) {
            bloodDonationsService.donorBloodDonationRequest(donorBloodDonationRequest);
    }

}
