package com.bds.BloodDonations;

import com.bds.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloodDonationsRepository extends JpaRepository<BloodDonations, Long> {

    @Query(value = "SELECT u.bloodType AS bloodType, SUM(bd.units) AS totalUnits "
            + "FROM blood_donations bd, users u WHERE bd.donor.id = u.id GROUP BY u.bloodType")
    List<BloodUnits> countTotalUnitsByBloodType();

    @Query(value = "SELECT count(bd.donor.id) = 1 FROM blood_donations bd WHERE bd.donor.id = :donorId "
           + "AND bd.donationDate = :donationDate")
    boolean existsBloodDonationsByDonorAndDonationDate(@Param("donorId") Long id,
                                                       @Param("donationDate") LocalDate donationDate);

    @Query(value = "SELECT count(bd.id) = 1 FROM blood_donations bd WHERE bd.id = :donationId")
    boolean existsBloodDonationsByDonationId(@Param("donationId") Long donationId);

    @Query(value = "SELECT bd.units FROM blood_donations bd WHERE bd.id = :donationId")
    Integer findUnitsByDonationId(@Param("donationId") Long donationId);
}