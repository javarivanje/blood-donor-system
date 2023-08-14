package com.bds.BloodDonationEvent;

import com.bds.exception.RequestValidationException;
import com.bds.user.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class BloodDonationEventService {

    private final BloodDonationEventRepository bloodDonationEventRepository;
    private final UsersRepository usersRepository;

    public BloodDonationEventService(BloodDonationEventRepository bloodDonationEventRepository, UsersRepository usersRepository) {
        this.bloodDonationEventRepository = bloodDonationEventRepository;
        this.usersRepository = usersRepository;
    }

    public BloodDonationEvent addBloodDonationEvent(BloodDonationEventRequest bloodDonationEventRequest) {

        Long id = bloodDonationEventRequest.users().getId();
        if(!usersRepository.existsById(id)) {
            throw new RequestValidationException("user with " + id + " does not exists");
        }

        BloodDonationEvent newEvent = new BloodDonationEvent(
                bloodDonationEventRequest.eventName(),
                bloodDonationEventRequest.eventDate(),
                bloodDonationEventRequest.bloodType(),
                bloodDonationEventRequest.units(),
                bloodDonationEventRequest.users());

        bloodDonationEventRepository.save(newEvent);
        return newEvent;
    }
}
