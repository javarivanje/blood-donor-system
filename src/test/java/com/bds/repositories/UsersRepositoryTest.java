package com.bds.repositories;

import com.bds.AbstractTestcontainers;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private UsersRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByRoleIs() {
        // Given
        Users users = new Users(
                "milos",
                "bacetic",
                "milos.bacetic@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );
        underTest.save(users);

        Users users2 = new Users(
                "nemanja",
                "stanojevic",
                "nemanja.stanojevic@gmail.com",
                Role.ADMIN,
                BloodType.BNeg
        );
        underTest.save(users2);

        // When
        List<Users> allUsers = underTest.findByRoleIs(Role.ADMIN);

        // Then
        assertThat(allUsers).allMatch(user -> user.getRole().equals(Role.ADMIN));
    }

    @Test
    void existsUsersByEmail() {
        // Given
        Users users = new Users(
                "milos",
                "bacetic",
                "milos.bacetic@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );
        underTest.save(users);

        // When
        boolean exists = underTest.existsUsersByEmail("milos.bacetic@gmail.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void findUsersByEmail() {
        // Given
        String email = "milos.bacetic@gmail.com";
        Users users = new Users(
                "milos",
                "bacetic",
                email,
                Role.ADMIN,
                BloodType.APos
        );
        underTest.save(users);

        // When
        Users milos = underTest.findUsersByEmail(email);

        // Then
        assertThat(milos.getEmail()).isEqualTo(email);
    }
}