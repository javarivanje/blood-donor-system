package com.bds.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/admin")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/donor")
    public List<Users> getAllDonors() {
        return usersService.getAllDonors();
    }

    @PostMapping("/register_user")
    public void registerNewUser(@RequestBody UsersRegistrationRequest request) {
        usersService.registerNewUser(request);
    }
}
