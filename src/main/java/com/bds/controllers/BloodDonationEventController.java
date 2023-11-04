package com.bds.controllers;

import com.bds.dto.BloodDonationEventRequest;
import com.bds.models.BloodDonationEvent;
import com.bds.services.BloodDonationEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
@Tag(name = "Blood Donation Event", description = "Blood donation events management API")
public class BloodDonationEventController {

    private final BloodDonationEventService bloodDonationEventService;

    public BloodDonationEventController(BloodDonationEventService bloodDonationEventService) {
        this.bloodDonationEventService = bloodDonationEventService;
    }

    @Operation(
            summary = "Retrieve a new blood donation event",
            description = "This is a endpoint for registering a blood donation event. " +
                    "The response is registered Blood Donation Event objects with it's properties",
            tags = {"Blood donation event", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(schema = @Schema(implementation = BloodDonationEvent.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})
    })
    @PostMapping("donation_event")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodDonationEvent> addBloodDonationEvent(
            @Parameter(description = "Blood donation event request")
            @RequestBody BloodDonationEventRequest bloodDonationEventRequest) {
        return new ResponseEntity(
                bloodDonationEventService.addBloodDonationEvent(bloodDonationEventRequest),
                HttpStatus.CREATED);
    }
}
