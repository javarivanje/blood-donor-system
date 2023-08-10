package com.bds.BloodDonations;

import com.bds.exception.DuplicateResourceException;
import com.bds.exception.RequestValidationException;
import com.bds.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodDonationsService {

    BloodDonationsRepository bloodDonationsRepository;

    public BloodDonationsService(BloodDonationsRepository bloodDonationsRepository) {
        this.bloodDonationsRepository = bloodDonationsRepository;
    }

    public List<BloodUnits> countTotalUnitsByBloodType() {
        return bloodDonationsRepository.countTotalUnitsByBloodType();
    }

    public void addBloodDonation(BloodDonationRequest bloodDonationRequest) {

        if (bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                bloodDonationRequest.donor().getId(),
                bloodDonationRequest.donationDate()
        )) {
            throw new DuplicateResourceException("donor or donation date already exists");
        }

        bloodDonationsRepository.save(
                new BloodDonations(
                        bloodDonationRequest.units(),
                        bloodDonationRequest.donationDate(),
                        bloodDonationRequest.donor(),
                        bloodDonationRequest.admin()
                )
        );

    }

    public void confirmBloodDonation(ConfirmDonationRequest confirmDonationRequest) {

        Long donationId = confirmDonationRequest.donationId();
        Boolean bloodDonationExists = bloodDonationsRepository.existsBloodDonationsByDonationId(donationId);

        if (!bloodDonationExists) {
            throw new ResourceNotFoundException("donation id does not exists");
        }

        BloodDonations donation = bloodDonationsRepository.getById(donationId);

        Integer confirmUnits = confirmDonationRequest.units();
        Integer units = bloodDonationsRepository.findUnitsByDonationId(donationId);
        if (confirmUnits != units) {
            throw new RequestValidationException("confirmed units does not match DONOR units");
        }

        donation.setUnits(confirmUnits);

    }
}
