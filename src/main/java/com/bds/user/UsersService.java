package com.bds.user;

import org.springframework.stereotype.Service;

import java.util.List;

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
}
