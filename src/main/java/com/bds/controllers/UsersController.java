package com.bds.controllers;

import com.bds.dto.UsersRegistrationRequest;
import com.bds.models.Users;
import com.bds.services.UsersService;
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
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users", description = "Users management API")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "Retrieve a list of donors",
            description = "This is a endpoint for getting all DONOR users." +
                    "The response is list of Users objects with it's properties",
            tags = {"Users", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content= { @Content(schema = @Schema(implementation = Users.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/donor")
    public ResponseEntity<List<Users>> getAllDonors() {
        return new ResponseEntity<>(usersService.getAllDonors(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a list of admins",
            description = "This is a endpoint for getting all ADMIN users." +
                    "The response is list of Users objects with it's properties",
            tags = {"Users", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content= { @Content(schema = @Schema(implementation = Users.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema()) })
    })
    @GetMapping()
    public ResponseEntity<List<Users>> getAllAdmins() {
        return new ResponseEntity<>(usersService.getAllAdmins(), HttpStatus.OK);
    }

    @Operation(
            summary = "Register new user",
            description = "This is a endpoint for registering new user",
            tags = {"Users", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized / Invalid Token", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "409", description = "Conflict, email already taken", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "406", description = "Request not validated", content = {@Content(schema = @Schema())})

    })
    @PostMapping("/register_user")
    public ResponseEntity<?> registerNewUser(
            @Parameter(description = "New user request")
            @RequestBody UsersRegistrationRequest request) {
        return new ResponseEntity<>(
                usersService.registerNewUser(request), HttpStatus.CREATED);
    }
}
