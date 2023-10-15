package com.bds.repositories;

import com.bds.AbstractTestcontainers;
import com.bds.dto.BloodUnits;
import com.bds.models.BloodDonations;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BloodDonationsRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private BloodDonationsRepository underTest;
    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void countAvailableUnitsByBloodType() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );

        usersRepository.saveAll(List.of(admin, donor));

        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );

        underTest.saveAll(List.of(bloodDonations));

        // When
        List<BloodUnits> bloodUnits = underTest.countAvailableUnitsByBloodType();

        // Then
        assertThat(bloodUnits).allMatch(bu ->
            bu.getBloodType().equals(BloodType.BNeg)
                    && bu.getTotalUnits().equals(3));
    }

    @Test
    void countAvailableUnitsByBloodTypeWhenNoBloodUnitIsPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );

        usersRepository.saveAll(List.of(admin, donor));

        BloodDonations bloodDonations = new BloodDonations(
                0,
                LocalDate.now(),
                donor,
                admin
        );

        underTest.saveAll(List.of(bloodDonations));

        // When
        List<BloodUnits> bloodUnits = underTest.countAvailableUnitsByBloodType();

        // Then
        assertThat(bloodUnits).allMatch(bu ->
                bu.getBloodType().equals(BloodType.BNeg)
                        && bu.getTotalUnits().equals(0));
    }

    @Test
    void existsBloodDonationsByDonorAndDonationDate() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long donorId = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonor().getId())
                .findFirst()
                .orElseThrow();

        LocalDate donationDate = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonationDate())
                .findFirst()
                .orElseThrow();

        // When

        boolean exists = underTest.existsBloodDonationsByDonorAndDonationDate(donorId, donationDate);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsBloodDonationsByDonorAndDonationDateWhenDonorIsNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long donorId = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonor().getId())
                .findFirst()
                .orElseThrow();

        LocalDate donationDate = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonationDate())
                .findFirst()
                .orElseThrow();

        // When

        boolean exists = underTest.existsBloodDonationsByDonorAndDonationDate(donorId+1, donationDate);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsBloodDonationsByDonorAndDonationDateWhenDonationDateIsNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long donorId = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonor().getId())
                .findFirst()
                .orElseThrow();

        LocalDate donationDate = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonationDate())
                .findFirst()
                .orElseThrow();

        // When

        boolean exists = underTest.existsBloodDonationsByDonorAndDonationDate(donorId, donationDate.minusDays(1L));

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsBloodDonationsByDonorAndDonationDateWhenDonorAndDonationDateAreNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long donorId = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonor().getId())
                .findFirst()
                .orElseThrow();

        LocalDate donationDate = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(units))
                .map(bd -> bd.getDonationDate())
                .findFirst()
                .orElseThrow();

        // When

        boolean exists = underTest.existsBloodDonationsByDonorAndDonationDate(donorId+1, donationDate.minusDays(1L));

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsBloodDonationsByDonationId() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long id = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(3))
                .map(BloodDonations::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean exists = underTest.existsBloodDonationsByDonationId(id);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsBloodDonationsByDonationIdWhenDonationIdIsNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long id = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(3))
                .map(BloodDonations::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean exists = underTest.existsBloodDonationsByDonationId(id+1);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findUnitsByDonationId() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                units,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long id = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(3))
                .map(BloodDonations::getId)
                .findFirst()
                .orElseThrow();
        // When
        Integer unitsByDonationId = underTest.findUnitsByDonationId(id);

        // Then
        assertThat(unitsByDonationId).isEqualTo(units);

    }

    @Test
    void findUnitsByDonationIdWhenDonationIdIsNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.saveAll(List.of(admin, donor));

        Integer units = 3;
        BloodDonations bloodDonations = new BloodDonations(
                units,
                LocalDate.now(),
                donor,
                admin
        );
        underTest.save(bloodDonations);

        Long id = underTest.findAll()
                .stream()
                .filter(bd -> bd.getUnits().equals(3))
                .map(BloodDonations::getId)
                .findFirst()
                .orElseThrow();
        // When
        Integer unitsByDonationId = underTest.findUnitsByDonationId(id+1);

        // Then
        assertThat(unitsByDonationId).isNull();

    }

    @Test
    void findByDonorId() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );
        usersRepository.save(admin);

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.save(donor);

        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                admin,
                donor
        );
        underTest.save(bloodDonations);

        // When
        Long id = usersRepository.findAll()
                .stream()
                .filter(bd -> bd.getEmail().equals("n.s@gmail.com"))
                .map(Users::getId)
                .findFirst()
                .orElseThrow();

        List<BloodDonations> donationsByDonorId = underTest.findByDonorId(id);

        // Then
        assertThat(donationsByDonorId).allMatch(d -> d.getId().equals(id));
    }

    @Test
    void findByDonorIdWhenDonorIdNotPresent() {
        // Given
        Users admin = new Users(
                "m",
                "b",
                "m.b@gmail.com",
                Role.ADMIN,
                BloodType.APos
        );
        usersRepository.save(admin);

        Users donor = new Users(
                "n",
                "s",
                "n.s@gmail.com",
                Role.DONOR,
                BloodType.BNeg
        );
        usersRepository.save(donor);

        BloodDonations bloodDonations = new BloodDonations(
                3,
                LocalDate.now(),
                admin,
                donor
        );
        underTest.save(bloodDonations);

        // When
        Long id = usersRepository.findAll()
                .stream()
                .filter(bd -> bd.getEmail().equals("n.s@gmail.com"))
                .map(Users::getId)
                .findFirst()
                .orElseThrow();

        List<BloodDonations> donationsByDonorId = underTest.findByDonorId(id+1);

        // Then
        assertThat(donationsByDonorId).isEmpty();
    }
}