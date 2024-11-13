package com.bds.controllers;

import com.bds.dto.UsersRegistrationRequest;
import com.bds.models.Users;
import com.bds.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/donor")
    public ResponseEntity<List<Users>> getAllDonors() {
        return new ResponseEntity<>(usersService.getAllDonors(), HttpStatus.OK);
    }

    @PostMapping("/register_user")
    public ResponseEntity<?> registerNewUser(@RequestBody UsersRegistrationRequest request) {
        return new ResponseEntity<>(usersService.registerNewUser(request), HttpStatus.CREATED);
    }
}
