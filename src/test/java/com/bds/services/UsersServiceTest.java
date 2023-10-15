package com.bds.services;

import com.bds.dto.UsersRegistrationRequest;
import com.bds.exception.DuplicateResourceException;
import com.bds.exception.ResourceNotFoundException;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.bds.repositories.UsersRepository;
import com.bds.validators.DtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private DtoValidator<UsersRegistrationRequest> validator;
    private UsersService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UsersService(usersRepository, validator);
    }

    @Test
    void willGetAllDonors() {
        // Given
        Role role = Role.DONOR;

        // When
        underTest.getAllDonors();

        // Then
        verify(usersRepository).findByRoleIs(role);
    }

    @Test
    void willCallValidator() {
        // Given
        UsersRegistrationRequest usersRegistrationRequest = new UsersRegistrationRequest(
                "milos",
                "bacetic",
                "milos.bacetic@gmail.com",
                "ADMIN",
                "APos"
        );

        // When
        underTest.registerNewUser(usersRegistrationRequest);

        // Then
        verify(validator).validate(usersRegistrationRequest);
    }

    @Test
    void willRegisterNewUser() {
        // Given
        UsersRegistrationRequest usersRegistrationRequest = new UsersRegistrationRequest(
                "milos",
                "bacetic",
                "milos.bacetic@gmail.com",
                "ADMIN",
                "APos"
        );

        Users newUser = new Users(
                usersRegistrationRequest.firstName(),
                usersRegistrationRequest.lastName(),
                usersRegistrationRequest.email(),
                Role.valueOf(usersRegistrationRequest.role()),
                BloodType.valueOf(usersRegistrationRequest.bloodType())
        );

        // When
        underTest.registerNewUser(usersRegistrationRequest);

        // Then
        ArgumentCaptor<Users> usersArgumentCaptor =
                ArgumentCaptor.forClass(Users.class);

        verify(usersRepository)
                .save(usersArgumentCaptor.capture());

        Users capturedUser = usersArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(newUser);
    }

    @Test
    void willThrowDuplicateResourceException() {
        // Given
        String email = "milos.bacetic@gmail.com";
        UsersRegistrationRequest usersRegistrationRequest = new UsersRegistrationRequest(
                "milos",
                "bacetic",
                email,
                "ADMIN",
                "APos"
        );

        Users newUser = new Users(
                usersRegistrationRequest.firstName(),
                usersRegistrationRequest.lastName(),
                usersRegistrationRequest.email(),
                Role.valueOf(usersRegistrationRequest.role()),
                BloodType.valueOf(usersRegistrationRequest.bloodType())
        );

        given(validator.validate(usersRegistrationRequest)).willReturn(true);
        given(usersRepository.existsUsersByEmail(email)).willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.registerNewUser(usersRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken");

        verify(usersRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void findUserByEmail() {
        // Given
        String email = "milos.bacetic@gmail.com";
        Users newUser = new Users(
                "milos",
                "bacetic",
                email,
                Role.ADMIN,
                BloodType.APos
        );

        // When
        underTest.findUserByEmail(email);

        // Then
        verify(usersRepository).findUsersByEmail(email);
    }

    @Test
    void willThrowResourceNotFoundException() {
        // Given
        String email = "milos.bacetic@gmail.com";
        Users newUser = new Users(
                "milos",
                "bacetic",
                email,
                Role.ADMIN,
                BloodType.APos
        );

        given(usersRepository.existsUsersByEmail(email))
                .willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.findUserByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user does not exists");
    }
}