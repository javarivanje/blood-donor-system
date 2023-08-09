package com.bds.user;

import com.bds.exception.DuplicateResourceException;
import com.bds.exception.RequestValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllDonors() {
        Role role = Role.DONOR;
        return usersRepository.findByRoleIs(role);
    }

    public void registerNewUser(UsersRegistrationRequest usersRegistrationRequest) {

        String email = usersRegistrationRequest.email();
        if (usersRepository.existsUsersByEmail(email)) {
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        usersRepository.save(
                new Users(
                    usersRegistrationRequest.firstName(),
                    usersRegistrationRequest.lastName(),
                    usersRegistrationRequest.email(),
                    usersRegistrationRequest.role(),
                    usersRegistrationRequest.bloodType()
                )
        );
    }
}
