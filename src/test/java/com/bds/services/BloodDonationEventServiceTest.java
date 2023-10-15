package com.bds.services;

import com.bds.dto.BloodDonationEventRequest;
import com.bds.exception.RequestValidationException;
import com.bds.models.BloodDonationEvent;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.bds.repositories.BloodDonationEventRepository;
import com.bds.repositories.UsersRepository;
import com.bds.validators.DtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class BloodDonationEventServiceTest {

    @Mock
    private BloodDonationEventRepository bloodDonationEventRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private DtoValidator<BloodDonationEventRequest> validator;

    private BloodDonationEventService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BloodDonationEventService(
                bloodDonationEventRepository,
                usersRepository,
                validator);
    }

    @Test
    void addBloodDonationEventWillCallValidator(){
        // Given
        String eventName = "blood donation event";
        LocalDate eventDate = LocalDate.now();
        String bloodType = "APos";
        Integer units = 5;
        Users admin = new Users(
                1L,
                "milos",
                "bacetic",
                "m@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        BloodDonationEventRequest bloodDonationEventRequest =
                new BloodDonationEventRequest(
                        eventName,
                        eventDate,
                        bloodType,
                        units,
                        admin
                );

        given(usersRepository.existsById(1L)).willReturn(true);

        // When
        underTest.addBloodDonationEvent(bloodDonationEventRequest);

        // Then
        verify(validator).validate(bloodDonationEventRequest);
    }

    @Test
    void willAddBloodDonationEvent() {
        // Given
        String eventName = "blood donation event";
        LocalDate eventDate = LocalDate.now();
        String bloodType = "APos";
        Integer units = 5;
        Users admin = new Users(
                "milos",
                "bacetic",
                "m@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        BloodDonationEventRequest bloodDonationEventRequest =
            new BloodDonationEventRequest(
                    eventName,
                    eventDate,
                    bloodType,
                    units,
                    admin
            );

        BloodDonationEvent newEvent = new BloodDonationEvent(
                bloodDonationEventRequest.eventName(),
                bloodDonationEventRequest.eventDate(),
                BloodType.valueOf(bloodDonationEventRequest.bloodType()),
                bloodDonationEventRequest.units(),
                bloodDonationEventRequest.users());

        given(usersRepository.existsById(admin.getId()))
                        .willReturn(true);

        // When
        underTest.addBloodDonationEvent(bloodDonationEventRequest);

        // Then
        ArgumentCaptor<BloodDonationEvent> bloodDonationEventArgumentCaptor =
                ArgumentCaptor.forClass(BloodDonationEvent.class);

        verify(bloodDonationEventRepository)
                .save(bloodDonationEventArgumentCaptor.capture());

        BloodDonationEvent captured = bloodDonationEventArgumentCaptor.getValue();

        assertThat(captured).isEqualTo(newEvent);
    }

    @Test
    void addBloodDonationEventWillThrowRequestValidationException() {
        // Given
        String eventName = "blood donation event";
        LocalDate eventDate = LocalDate.now();
        String bloodType = "APos";
        Integer units = 5;
        Users admin = new Users(
                "milos",
                "bacetic",
                "m@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        BloodDonationEventRequest bloodDonationEventRequest =
                new BloodDonationEventRequest(
                        eventName,
                        eventDate,
                        bloodType,
                        units,
                        admin
                );

        given(usersRepository.existsById(admin.getId()))
                .willReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addBloodDonationEvent(bloodDonationEventRequest))
                .isInstanceOf(RequestValidationException.class);

        verify(bloodDonationEventRepository, never()).save(any());
    }
}