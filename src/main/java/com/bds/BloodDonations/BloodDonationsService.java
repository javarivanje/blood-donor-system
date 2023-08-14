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

    public List<BloodUnits> countAvailableUnitsByBloodType() {
        return bloodDonationsRepository.countAvailableUnitsByBloodType();
    }

    public BloodDonations addBloodDonation(BloodDonationRequest bloodDonationRequest) {

        if (bloodDonationsRepository.existsBloodDonationsByDonorAndDonationDate(
                bloodDonationRequest.donor().getId(),
                bloodDonationRequest.donationDate()
        )) {
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

    public void initiateBloodDonation(InitiateBloodDonationRequest initiateBloodDonationRequest) {

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
        return bloodDonationsRepository.findByDonorId(donorId);
    }

    public void donorBloodDonationRequest(DonorBloodDonationRequest donorBloodDonationRequest) {


        bloodDonationsRepository.save(
                new BloodDonations(
                        donorBloodDonationRequest.units(),
                        donorBloodDonationRequest.donationDate(),
                        donorBloodDonationRequest.donor())
                );

    }
}
