package com.bds.repositories;

import com.bds.models.BloodDonationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodDonationEventRepository extends JpaRepository<BloodDonationEvent, Long> {
}
