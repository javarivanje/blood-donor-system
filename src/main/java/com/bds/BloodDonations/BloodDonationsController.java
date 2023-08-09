package com.bds.BloodDonations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class BloodDonationsController {

    private final BloodDonationsService bloodDonationsService;

    public BloodDonationsController(BloodDonationsService bloodDonationsService) {
        this.bloodDonationsService = bloodDonationsService;
    }

    @GetMapping("/admin/available_blood_units")
    public List<BloodUnitsCount> countTotalUnitsByBloodType() {
        return bloodDonationsService.countTotalUnitsByBloodType();
    }
}
