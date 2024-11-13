package com.bds.controllers;

import com.bds.dto.BloodDonationEventRequest;
import com.bds.models.BloodDonationEvent;
import com.bds.services.BloodDonationEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
public class BloodDonationEventController {

    private final BloodDonationEventService bloodDonationEventService;

    public BloodDonationEventController(BloodDonationEventService bloodDonationEventService) {
        this.bloodDonationEventService = bloodDonationEventService;
    }

    @PostMapping("donation_event")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodDonationEvent> addBloodDonationEvent(
            @RequestBody BloodDonationEventRequest bloodDonationEventRequest) {
        return new ResponseEntity(
                bloodDonationEventService.addBloodDonationEvent(bloodDonationEventRequest),
                HttpStatus.CREATED);
    }

}
