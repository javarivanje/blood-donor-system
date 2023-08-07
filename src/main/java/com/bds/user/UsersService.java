package com.bds.user;

import com.bds.exception.DuplicateResourceException;
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

    public void registerNewUser(UsersRegistrationRequest request) {
        String email = request.email();
        if (usersRepository.existsUsersByEmail(email)) {
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        Users users = new Users(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.role(),
                request.bloodType());

        usersRepository.save(users);
    }
}
