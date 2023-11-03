package com.bds.services;

import com.bds.dto.UsersRegistrationRequest;
import com.bds.exception.DuplicateResourceException;
import com.bds.exception.ResourceNotFoundException;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.bds.repositories.UsersRepository;
import com.bds.validators.DtoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final DtoValidator<UsersRegistrationRequest> validator;

    public UsersService(UsersRepository usersRepository, DtoValidator<UsersRegistrationRequest> validator) {
        this.usersRepository = usersRepository;
        this.validator = validator;
    }

    public List<Users> getAllDonors() {
        Role role = Role.DONOR;
        return usersRepository.findByRoleIs(role);
    }

    public List<Users> getAllAdmins() {
        Role role = Role.ADMIN;
        return usersRepository.findByRoleIs(role);
    }

    public Users registerNewUser(UsersRegistrationRequest usersRegistrationRequest) {
        boolean validated = validator.validate(usersRegistrationRequest);

        if (validated) {
            String email = usersRegistrationRequest.email();
            if (usersRepository.existsUsersByEmail(email)) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
        }

        Users newUser = new Users(
                usersRegistrationRequest.firstName(),
                usersRegistrationRequest.lastName(),
                usersRegistrationRequest.email(),
                Role.valueOf(usersRegistrationRequest.role()),
                BloodType.valueOf(usersRegistrationRequest.bloodType())
        );
        usersRepository.save(newUser);
        return newUser;
    }

    public Users findUserByEmail(String email) {
        if(!usersRepository.existsUsersByEmail(email)) {
            throw new ResourceNotFoundException(
                    "user does not exists"
            );
        }
        return usersRepository.findUsersByEmail(email);
    }
}
