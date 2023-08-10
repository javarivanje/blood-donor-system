package com.bds.BloodDonations;


import com.bds.user.Users;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "blood_donations")
@Table(
        name = "blood_donations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "blood_donations_unique",
                        columnNames = {"donor_id", "donation_date"})
        }
)
public class BloodDonations {

    @Id
    @SequenceGenerator(
            name = "blood_donations_id_seq",
            sequenceName = "blood_donations_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blood_donations_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long Id;

    @Column(
            name = "units",
            nullable = false
    )
    private Integer units;

    @Column(
            name = "donation_date",
            nullable = false
    )
    private LocalDate donationDate;

    @ManyToOne
    @JoinColumn(
            name = "donor_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_donor_id"
            )
    )
    private Users donor;

    @ManyToOne
    @JoinColumn(
            name = "admin_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_admin_id"
            )
    )
    private Users admin;

    public BloodDonations() {
    }

    public BloodDonations(Long id, Integer units, LocalDate donationDate, Users donor, Users admin) {
        this.Id = id;
        this.units = units;
        this.donationDate = donationDate;
        this.donor = donor;
        this.admin = admin;
    }

    public BloodDonations(Integer units, LocalDate donationDate, Users donor, Users admin) {
        this.units = units;
        this.donationDate = donationDate;
        this.donor = donor;
        this.admin = admin;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }

    public Users getDonor() {
        return donor;
    }

    public void setDonor(Users donor) {
        this.donor = donor;
    }

    public Users getAdmin() {
        return admin;
    }

    public void setAdmin(Users admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodDonations that = (BloodDonations) o;
        return Objects.equals(Id, that.Id) && Objects.equals(units, that.units) && Objects.equals(donationDate, that.donationDate) && Objects.equals(donor, that.donor) && Objects.equals(admin, that.admin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, units, donationDate, donor, admin);
    }

    @Override
    public String toString() {
        return "blood_donations{" +
                "Id=" + Id +
                ", units=" + units +
                ", donationDate=" + donationDate +
                ", donor=" + donor +
                ", admin=" + admin +
                '}';
    }
}
