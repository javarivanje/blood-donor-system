package com.bds.user;


import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "users_email_unique",
                        columnNames = "email"
                )
        }
)
public class Users {

    @Id
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long Id;

    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Column(
            name = "role",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(
            name = "blood_type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    public Users() {
    }

    public Users(Long Id, String firstName, String lastName, String email, Role role, BloodType bloodType) {
        this.Id = Id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.bloodType = bloodType;
    }

    public Users(String firstName, String lastName, String email, Role role, BloodType bloodType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.bloodType = bloodType;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(Id, users.Id) && Objects.equals(firstName, users.firstName)
                && Objects.equals(lastName, users.lastName) && Objects.equals(email, users.email)
                && Objects.equals(role, users.role) && Objects.equals(bloodType, users.bloodType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, firstName, lastName, email, role, bloodType);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", bloodType=" + bloodType +
                '}';
    }
}
