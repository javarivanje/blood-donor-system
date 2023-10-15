package com.bds.services;

import com.bds.dto.BloodDonationRequest;
import com.bds.exception.DuplicateResourceException;
import com.bds.models.BloodDonations;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.bds.repositories.BloodDonationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BloodDonationsServiceTest {

    @Mock
    private BloodDonationsRepository bloodDonationsRepository;
    private BloodDonationsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BloodDonationsService(bloodDonationsRepository);
    }

    @Test
    void countAvailableUnitsByBloodType() {
        // Given

        // When
        underTest.countAvailableUnitsByBloodType();

        // Then
        verify(bloodDonationsRepository).countAvailableUnitsByBloodType();
    }

    @Test
    void willAddBloodDonation() {
        // Given
        BloodDonationRequest bloodDonationRequest = new BloodDonationRequest(
                5,
                LocalDate.now(),
                 new Users(
                "milos",
                "bacetic",
                "milos@gmail.com",
                Role.ADMIN,
                BloodType.APos
                ),
                new Users(
                "nemanja",
                "nemanjic",
                "nemanja@gmail.com",
                Role.DONOR,
                BloodType.BNeg
                )
        );

        BloodDonations newDonation = new BloodDonations(
                bloodDonationRequest.units(),
                bloodDonationRequest.donationDate(),
                bloodDonationRequest.donor(),
                bloodDonationRequest.admin()
        );
        // When
        underTest.addBloodDonation(bloodDonationRequest);

        // Then
        verify(bloodDonationsRepository).save(newDonation);
    }

    @Test
    void willThrowResourceNotFoundException() {
        // Given
        BloodDonationRequest bloodDonationRequest = new BloodDonationRequest(
                5,
                LocalDate.now(),
                new Users(
                        "milos",
                        "bacetic",
                        "milos@gmail.com",
                        Role.ADMIN,
                        BloodType.APos
                ),
                new Users(
                        "nemanja",
                        "nemanjic",
                        "nemanja@gmail.com",
                        Role.DONOR,
                        BloodType.BNeg
                )
        );

        BloodDonations newDonation = new BloodDonations(
                bloodDonationRequest.units(),
                bloodDonationRequest.donationDate(),
                bloodDonationRequest.donor(),
                bloodDonationRequest.admin()
        );

        given(bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                        bloodDonationRequest.donor().getId(),
                        bloodDonationRequest.donationDate()))
                .willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addBloodDonation(bloodDonationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("donor or donation date already exists");
    }

    @Test
    void confirmBloodDonation() {
        // Given

        // When

        // Then
    }

    @Test
    void initiateBloodDonation() {
        // Given

        // When

        // Then
    }

    @Test
    void getBloodDonations() {
        // Given

        // When

        // Then
    }

    @Test
    void donorBloodDonationRequest() {
        // Given

        // When

        // Then
    }
}