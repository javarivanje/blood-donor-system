package com.bds.user;

public record UsersRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Role role,
        BloodType bloodType
) {
}
