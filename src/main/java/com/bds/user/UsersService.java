package com.bds.user;

import com.bds.exception.DuplicateResourceException;
import com.bds.validators.ObjectsValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final ObjectsValidator<UsersRegistrationRequest> validator;

    public UsersService(UsersRepository usersRepository, ObjectsValidator<UsersRegistrationRequest> validator) {
        this.usersRepository = usersRepository;
        this.validator = validator;
    }

    public List<Users> getAllDonors() {
        Role role = Role.DONOR;
        return usersRepository.findByRoleIs(role);
    }

    public Users registerNewUser(UsersRegistrationRequest usersRegistrationRequest) {
        validator.validate(usersRegistrationRequest);

        String email = usersRegistrationRequest.email();
        if (usersRepository.existsUsersByEmail(email)) {
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        Users newUser = new Users(
                usersRegistrationRequest.firstName(),
                usersRegistrationRequest.lastName(),
                usersRegistrationRequest.email(),
                usersRegistrationRequest.role(),
                usersRegistrationRequest.bloodType()
        );
        usersRepository.save(newUser);
        return newUser;
    }
}
