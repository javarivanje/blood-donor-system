package com.bds.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
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
