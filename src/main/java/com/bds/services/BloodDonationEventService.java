package com.bds.services;

import com.bds.dto.BloodDonationEventRequest;
import com.bds.exception.RequestValidationException;
import com.bds.models.BloodDonationEvent;
import com.bds.models.BloodType;
import com.bds.repositories.BloodDonationEventRepository;
import com.bds.repositories.UsersRepository;
import com.bds.validators.DtoValidator;
import org.springframework.stereotype.Service;

@Service
public class BloodDonationEventService {

    private final BloodDonationEventRepository bloodDonationEventRepository;
    private final UsersRepository usersRepository;
    private final DtoValidator<BloodDonationEventRequest> validator;

    public BloodDonationEventService(BloodDonationEventRepository bloodDonationEventRepository, UsersRepository usersRepository, DtoValidator<BloodDonationEventRequest> validator) {
        this.bloodDonationEventRepository = bloodDonationEventRepository;
        this.usersRepository = usersRepository;
        this.validator = validator;
    }

    public BloodDonationEvent addBloodDonationEvent(BloodDonationEventRequest bloodDonationEventRequest) {
        validator.validate(bloodDonationEventRequest);

        Long id = bloodDonationEventRequest.users().getId();
        if (!usersRepository.existsById(id)) {
            throw new RequestValidationException("user with " + id + " does not exists");
        }

        BloodDonationEvent newEvent = new BloodDonationEvent(
                bloodDonationEventRequest.eventName(),
                bloodDonationEventRequest.eventDate(),
                BloodType.valueOf(bloodDonationEventRequest.bloodType()),
                bloodDonationEventRequest.units(),
                bloodDonationEventRequest.users());

        bloodDonationEventRepository.save(newEvent);
        return newEvent;
    }
}
