package com.bds.services;

import com.bds.dto.*;
import com.bds.exception.DuplicateResourceException;
import com.bds.exception.RequestValidationException;
import com.bds.exception.ResourceNotFoundException;
import com.bds.models.BloodDonations;
import com.bds.dto.BloodUnits;
import com.bds.repositories.BloodDonationsRepository;
import com.bds.repositories.UsersRepository;
import com.bds.validators.DtoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodDonationsService {

    private final BloodDonationsRepository bloodDonationsRepository;
    private final UsersRepository usersRepository;
    private final DtoValidator<Object> validator;

    public BloodDonationsService(BloodDonationsRepository bloodDonationsRepository, UsersRepository usersRepository, DtoValidator<Object> validator) {
        this.bloodDonationsRepository = bloodDonationsRepository;
        this.usersRepository = usersRepository;
        this.validator = validator;
    }

    public List<BloodUnits> countAvailableUnitsByBloodType() {
        return bloodDonationsRepository.countAvailableUnitsByBloodType();
    }

    public BloodDonations addBloodDonation(BloodDonationRequest bloodDonationRequest) {
        validator.validate(bloodDonationRequest);
        if (bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                bloodDonationRequest.donor().getId(),
                bloodDonationRequest.donationDate())
        ) {
            throw new DuplicateResourceException("donor or donation date already exists");
        }

        BloodDonations newDonation = new BloodDonations(
                bloodDonationRequest.units(),
                bloodDonationRequest.donationDate(),
                bloodDonationRequest.donor(),
                bloodDonationRequest.admin()
        );
        bloodDonationsRepository.save(newDonation);

        return newDonation;
    }

    public void confirmBloodDonation(Long donationId, ConfirmDonationRequest confirmDonationRequest) {
            validator.validate(confirmDonationRequest);
            BloodDonations savedBloodDonations = bloodDonationsRepository.findById(donationId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "donation id does not exists"
                    ));

            if (!bloodDonationsRepository.findUnitsByDonationId(donationId)
                    .equals(confirmDonationRequest.units())) {
                throw new RequestValidationException(
                        "confirmed units does not match DONOR units"
                );
            }

            bloodDonationsRepository
                    .save(savedBloodDonations);
    }

    public void initiateBloodDonation(InitiateBloodDonationRequest initiateBloodDonationRequest) {
        validator.validate(initiateBloodDonationRequest);
        if (bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                initiateBloodDonationRequest.donor().getId(),
                initiateBloodDonationRequest.donationDate()
        )) {
            throw new DuplicateResourceException("donor or donation date already exists");
        }

        bloodDonationsRepository.save(
                new BloodDonations(
                        initiateBloodDonationRequest.units(),
                        initiateBloodDonationRequest.donationDate(),
                        initiateBloodDonationRequest.donor()
                )
        );
    }

    public List<BloodDonations> getBloodDonations(Long donorId) {
        if(!usersRepository.findById(donorId).isPresent()) {
            throw new ResourceNotFoundException("Donor with given id: " + donorId + " does not exist");
        }

        List<BloodDonations> bloodDonationsList =
                bloodDonationsRepository.findByDonorId(donorId);
        if (bloodDonationsList.isEmpty()) {
            throw new ResourceNotFoundException("Donor with given id: " + donorId + " does not have donations");
        }
        return bloodDonationsList;
    }

    public void donorBloodDonationRequest(DonorBloodDonationRequest donorBloodDonationRequest) {
        validator.validate(donorBloodDonationRequest);
        bloodDonationsRepository.save(
                new BloodDonations(
                        donorBloodDonationRequest.units(),
                        donorBloodDonationRequest.donationDate(),
                        donorBloodDonationRequest.donor())
        );
    }
}
