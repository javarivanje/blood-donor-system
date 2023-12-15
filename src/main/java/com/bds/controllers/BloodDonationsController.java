package com.bds.controllers;

import com.bds.dto.BloodDonationRequest;
import com.bds.dto.BloodUnits;
import com.bds.dto.ConfirmDonationRequest;
import com.bds.dto.InitiateBloodDonationRequest;
import com.bds.models.BloodDonations;
import com.bds.services.BloodDonationsService;
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

import java.util.List;

@RestController
@RequestMapping("api/v1")
@Tag(name = "Blood donations", description = "Blood donations management API")
public class BloodDonationsController {

    private final BloodDonationsService bloodDonationsService;

    public BloodDonationsController(BloodDonationsService bloodDonationsService) {
        this.bloodDonationsService = bloodDonationsService;
    }

    @Operation(
            summary = "Retrieve a list of available blood unis",
            description = "This is a endpoint for getting all available blood units." +
                    "The response is list of BloodUnits objects with it's properties",
            tags = {"Blood donations", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema(implementation = BloodUnits.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/admin/available_blood_units")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodUnits>> countAvailableUnitsByBloodType() {
        return new ResponseEntity<>(bloodDonationsService.countAvailableUnitsByBloodType(),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Enter blood donation",
            description = "This is a endpoint for posting blood donation." +
                    "The response is added BloodDonations objects with it's properties",
            tags = {"Blood donations", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(schema = @Schema(implementation = BloodDonations.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Donor or donation date already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})

    })
    @PostMapping("/admin/enter_donation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodDonations> addBloodDonation(
            @Parameter(description = "New blood donation request")
            @RequestBody BloodDonationRequest bloodDonationRequest) {

        return new ResponseEntity<>(
                bloodDonationsService.addBloodDonation(bloodDonationRequest),
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Confirm blood donation",
            description = "This is a endpoint for ADMIN user confirming DONOR initiated blood donation.",
            tags = {"Blood donations", "patch"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Confirmed units does not match DONOR units", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Donation id does not exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Donor or donation date already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})

    })
    @PatchMapping("/admin/confirm_blood_donation/{donationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void confirmBloodDonation(
            @Parameter(description = "Donation id", required = true)
            @PathVariable("donationId") Long donationId,
            @Parameter(description = "Confirm donation request")
            @RequestBody ConfirmDonationRequest confirmDonationRequest) {
        bloodDonationsService.confirmBloodDonation(donationId, confirmDonationRequest);
    }

    @Operation(
            summary = "Initiate blood donation by DONOR user",
            description = "This is a endpoint for posting blood donation initiation.",
            tags = {"Blood donations", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "409", description = "Donor or donation date already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})

    })
    @PostMapping("/donor/initiate_blood_donation")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR')")
    public void initiateBloodDonation(
            @Parameter(description = "Initiate blood donation request")
            @RequestBody InitiateBloodDonationRequest initiateBloodDonationRequest) {
        bloodDonationsService.initiateBloodDonation(initiateBloodDonationRequest);
    }

    @Operation(
            summary = "Retrieve a list of blood donations of donor with given id",
            description = "This is a endpoint for getting a list of blood donations of donor with given id." +
                    "The response is list of BloodDonations objects with it's properties",
            tags = {"Blood donations", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = BloodDonations.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/donor/my_blood_donations/{donorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR')")
    public List<BloodDonations> getDonorBloodDonations(
            @Parameter(description = "Donor id", required = true)
            @PathVariable("donorId") Long donorId) {
        return bloodDonationsService.getBloodDonations(donorId);
    }
}
