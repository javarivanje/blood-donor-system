package com.bds.BloodDonations;

import com.bds.BloodDonationEvent.BloodDonationEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodDonationsService {

    BloodDonationsRepository bloodDonationsRepository;
    public BloodDonationsService(BloodDonationsRepository bloodDonationsRepository) {
        this.bloodDonationsRepository = bloodDonationsRepository;
    }

    public List<BloodUnitsCount> countTotalUnitsByBloodType() {
        return bloodDonationsRepository.countTotalUnitsByBloodType();
    }
}
