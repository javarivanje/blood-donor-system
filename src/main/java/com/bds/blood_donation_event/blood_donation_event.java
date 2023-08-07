package com.bds.blood_donation_event;


import com.bds.user.BloodType;
import com.bds.user.Users;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "blood_donation_event")
@Table(
        name = "blood_donation_event"
)
public class blood_donation_event {

    @Id
    @SequenceGenerator(
            name = "blood_donation_event_id_seq",
            sequenceName = "blood_donation_event_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blood_donation_event_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long Id;

    @Column(
            name = "event_name",
            nullable = false
    )
    private String eventName;

    @Column(
            name = "event_date",
            nullable = false
    )
    private LocalDate eventDate;

    @Column(
            name = "blood_type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private BloodType blood_type;

    @ManyToOne
    @JoinColumn(
            name = "organizer_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_organizer_id"
            )
    )
    private Users users;

    public blood_donation_event(Long id, String eventName, LocalDate eventDate, BloodType blood_type, Users users) {
        Id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.blood_type = blood_type;
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public blood_donation_event() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public BloodType getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(BloodType blood_type) {
        this.blood_type = blood_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        blood_donation_event that = (blood_donation_event) o;
        return Objects.equals(Id, that.Id) && Objects.equals(eventName, that.eventName) && Objects.equals(eventDate, that.eventDate) && blood_type == that.blood_type && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, eventName, eventDate, blood_type, users);
    }

    @Override
    public String toString() {
        return "blood_donation_event{" +
                "Id=" + Id +
                ", eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", blood_type=" + blood_type +
                ", users=" + users +
                '}';
    }
}
