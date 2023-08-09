package com.bds.BloodDonationEvent;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
public class BloodDonationEventController {

    private final BloodDonationEventService bloodDonationEventService;

    public BloodDonationEventController(BloodDonationEventService bloodDonationEventService) {
        this.bloodDonationEventService = bloodDonationEventService;
    }

    @PostMapping("donation_event")
    public void addBloodDonationEvent(
            @RequestBody BloodDonationEventRequest bloodDonationEventRequest) {
        bloodDonationEventService.addBloodDonationEvent(bloodDonationEventRequest);
    }

}
