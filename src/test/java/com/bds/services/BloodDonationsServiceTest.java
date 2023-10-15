package com.bds.services;

import com.bds.dto.BloodDonationRequest;
import com.bds.dto.ConfirmDonationRequest;
import com.bds.dto.DonorBloodDonationRequest;
import com.bds.dto.InitiateBloodDonationRequest;
import com.bds.exception.DuplicateResourceException;
import com.bds.exception.RequestValidationException;
import com.bds.exception.ResourceNotFoundException;
import com.bds.models.BloodDonations;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.bds.repositories.BloodDonationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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

        given(bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                bloodDonationRequest.donor().getId(),
                bloodDonationRequest.donationDate()))
                .willReturn(false);

        // When
        underTest.addBloodDonation(bloodDonationRequest);

        // Then
        ArgumentCaptor<BloodDonations> bloodDonationsRequestCaptor =
                ArgumentCaptor.forClass(
                        BloodDonations.class
                );

        verify(bloodDonationsRepository).save(
                bloodDonationsRequestCaptor.capture()
        );

        BloodDonations capturedBloodDonations =
                bloodDonationsRequestCaptor.getValue();

        assertThat(capturedBloodDonations.getDonationDate())
                .isEqualTo(bloodDonationRequest.donationDate());
        assertThat(capturedBloodDonations.getUnits())
                .isEqualTo(bloodDonationRequest.units());
        assertThat(capturedBloodDonations.getDonor())
                .isEqualTo(bloodDonationRequest.donor());
        assertThat(capturedBloodDonations.getAdmin())
                .isEqualTo(bloodDonationRequest.admin());
    }

    @Test
    void addBloodDonationWillThrowResourceNotFoundException() {
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

        given(bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                        bloodDonationRequest.donor().getId(),
                        bloodDonationRequest.donationDate()))
                .willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addBloodDonation(bloodDonationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("donor or donation date already exists");

        verify(bloodDonationsRepository, never()).save(any());
    }

    @Test
    void willConfirmBloodDonation() {
        // Given
        Long donationId = 1L;
        Integer units = 5;
        BloodDonations bloodDonations = new BloodDonations(
                donationId,
                units,
                LocalDate.now(),
                new Users(
                        "nemanja",
                        "nemanjic",
                        "nemanja@gmail.com",
                        Role.DONOR,
                        BloodType.BNeg
                ),
                new Users(
                        "milos",
                        "bacetic",
                        "milos@gmail.com",
                        Role.ADMIN,
                        BloodType.APos
                )
        );

        ConfirmDonationRequest confirmDonationRequest = new ConfirmDonationRequest(
                1L,
                units
        );

        given(bloodDonationsRepository.findById(donationId)).willReturn(Optional.of(bloodDonations));
        given(bloodDonationsRepository.findUnitsByDonationId(donationId))
                .willReturn(confirmDonationRequest.units());

        // When
        underTest.confirmBloodDonation(donationId, confirmDonationRequest);

        // Then
        ArgumentCaptor<BloodDonations> bloodDonationsArgumentCaptor =
                ArgumentCaptor.forClass(BloodDonations.class);

        verify(bloodDonationsRepository)
                .save(bloodDonationsArgumentCaptor.capture());

        BloodDonations captured = bloodDonationsArgumentCaptor.getValue();

        assertThat(captured.getUnits()).isEqualTo(units);
    }

    @Test
    void confirmBloodDonationWillThrowResourceNotFoundException() {
        // Given
        Long donationId = 1L;
        Integer units = 5;

        ConfirmDonationRequest confirmDonationRequest = new ConfirmDonationRequest(
                1L,
                units
        );

        given(bloodDonationsRepository.findById(donationId)).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.confirmBloodDonation(donationId, confirmDonationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("donation id does not exists");

        verify(bloodDonationsRepository, never()).save(any());
    }

    @Test
    void confirmBloodDonationWillThrowRequestValidationException() {
    // Given
        Long donationId = 1L;
        Integer units = 5;
        BloodDonations bloodDonations = new BloodDonations(
                donationId,
                units,
                LocalDate.now(),
                new Users(
                        "n",
                        "n",
                        "n@gmail.com",
                        Role.DONOR,
                        BloodType.BNeg
                ),
                new Users(
                        "m",
                        "b",
                        "m@gmail.com",
                        Role.ADMIN,
                        BloodType.APos
                )
        );

        ConfirmDonationRequest confirmDonationRequest = new ConfirmDonationRequest(
                1L,
                units
        );

        given(bloodDonationsRepository.findById(donationId)).willReturn(Optional.of(bloodDonations));
        given(bloodDonationsRepository.findUnitsByDonationId(donationId)).willReturn(confirmDonationRequest.units()+1);

        // Then
        assertThatThrownBy(() -> underTest.confirmBloodDonation(donationId, confirmDonationRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("confirmed units does not match DONOR units");

        verify(bloodDonationsRepository, never()).save(any());
    }

    @Test
    void willInitiateNewBloodDonation() {
        // Given
        InitiateBloodDonationRequest initiateBloodDonationRequest = new InitiateBloodDonationRequest(
                new Users(
                    "milos",
                    "bacetic",
                    "milos.bacetic@gmail.com",
                    Role.DONOR,
                    BloodType.APos
        ),
                1,
                LocalDate.now()
        );

        // When
        underTest.initiateBloodDonation(initiateBloodDonationRequest);

        // Then
        ArgumentCaptor<BloodDonations> bloodDonationsCaptor =
                ArgumentCaptor.forClass(
                        BloodDonations.class
                );

        verify(bloodDonationsRepository).save(
                bloodDonationsCaptor.capture()
        );

        BloodDonations capturedBloodDonations =
                bloodDonationsCaptor.getValue();

        assertThat(capturedBloodDonations.getUnits()).isEqualTo(initiateBloodDonationRequest.units());
        assertThat(capturedBloodDonations.getDonationDate()).isEqualTo(initiateBloodDonationRequest.donationDate());
        assertThat(capturedBloodDonations.getDonor()).isEqualTo(initiateBloodDonationRequest.donor());
    }

    @Test
    void initiateNewBloodDonationWillThrowDuplicateResourceException() {
        // Given
        InitiateBloodDonationRequest initiateBloodDonationRequest = new InitiateBloodDonationRequest(
                new Users(
                        "milos",
                        "bacetic",
                        "milos.bacetic@gmail.com",
                        Role.DONOR,
                        BloodType.APos
                ),
                1,
                LocalDate.now()
        );

        given(bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                    initiateBloodDonationRequest.donor().getId(),
                    initiateBloodDonationRequest.donationDate()))
                .willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.initiateBloodDonation(initiateBloodDonationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("donor or donation date already exists");

        verify(bloodDonationsRepository, never()).save(any());
    }

    @Test
    void getBloodDonations() {
        // Given
        Long donorId = 1L;

        // When
        underTest.getBloodDonations(donorId);

        // Then
        verify(bloodDonationsRepository).findByDonorId(donorId);
    }

    @Test
    void willAddNewDonorBloodDonationRequest() {
        // Given
        Users donor = new Users(
                "milos",
                "bacetic",
                "milos.bacetic@gmail.com",
                Role.DONOR,
                BloodType.APos
        );

        DonorBloodDonationRequest donorBloodDonationRequest = new DonorBloodDonationRequest(
                donor,
                1,
                LocalDate.now()
        );

        // When
        underTest.donorBloodDonationRequest(donorBloodDonationRequest);

        // Then
        ArgumentCaptor<BloodDonations> donorBloodDonationsRequestCaptor =
                ArgumentCaptor.forClass(BloodDonations.class);

        verify(bloodDonationsRepository).save(donorBloodDonationsRequestCaptor.capture());

        BloodDonations captured = donorBloodDonationsRequestCaptor.getValue();

        assertThat(captured.getUnits()).isEqualTo(donorBloodDonationRequest.units());
        assertThat(captured.getDonationDate()).isEqualTo(donorBloodDonationRequest.donationDate());
        assertThat(captured.getDonor()).isEqualTo(donorBloodDonationRequest.donor());
    }
}