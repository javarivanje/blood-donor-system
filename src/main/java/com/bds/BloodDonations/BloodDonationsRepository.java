package com.bds.BloodDonations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodDonationsRepository extends JpaRepository<BloodDonations, Long> {

    @Query(value = "SELECT new com.bds.BloodDonations.BloodUnitsCount(u.bloodType, SUM(bd.units)) AS bloodUnits"
            + " FROM blood_donations bd, users u WHERE bd.donor.id = u.id GROUP BY u.bloodType")
    List<BloodUnitsCount>  countTotalUnitsByBloodType();
}
