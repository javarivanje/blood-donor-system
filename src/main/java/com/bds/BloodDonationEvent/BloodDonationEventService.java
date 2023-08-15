package com.bds.BloodDonationEvent;

import com.bds.exception.RequestValidationException;
import com.bds.user.UsersRepository;
import com.bds.validators.ObjectsValidator;
import org.springframework.stereotype.Service;

@Service
public class BloodDonationEventService {

    private final BloodDonationEventRepository bloodDonationEventRepository;
    private final UsersRepository usersRepository;
    private final ObjectsValidator<BloodDonationEventRequest> validator;

    public BloodDonationEventService(BloodDonationEventRepository bloodDonationEventRepository, UsersRepository usersRepository, ObjectsValidator<BloodDonationEventRequest> validator) {
        this.bloodDonationEventRepository = bloodDonationEventRepository;
        this.usersRepository = usersRepository;
        this.validator = validator;
    }

    public BloodDonationEvent addBloodDonationEvent(BloodDonationEventRequest bloodDonationEventRequest) {
        validator.validate(bloodDonationEventRequest);

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
