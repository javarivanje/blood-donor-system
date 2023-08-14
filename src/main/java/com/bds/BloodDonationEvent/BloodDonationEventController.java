package com.bds.BloodDonationEvent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("api/v1/admin")
public class BloodDonationEventController {

    private final BloodDonationEventService bloodDonationEventService;

    public BloodDonationEventController(BloodDonationEventService bloodDonationEventService) {
        this.bloodDonationEventService = bloodDonationEventService;
    }

    @PostMapping("donation_event")
    public ResponseEntity<BloodDonationEvent> addBloodDonationEvent(
            @RequestBody BloodDonationEventRequest bloodDonationEventRequest) {
        return new ResponseEntity(
                bloodDonationEventService.addBloodDonationEvent(bloodDonationEventRequest),
                HttpStatus.CREATED);
    }

}
