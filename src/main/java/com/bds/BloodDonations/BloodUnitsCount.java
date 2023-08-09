package com.bds.BloodDonations;

import com.bds.user.BloodType;

import java.util.Objects;

public class BloodUnitsCount {
    private String bloodType;
    private Integer units;

    public BloodUnitsCount() {
    }

    public BloodUnitsCount(String bloodType, Integer units) {
        this.bloodType = bloodType;
        this.units = units;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodUnitsCount that = (BloodUnitsCount) o;
        return Objects.equals(bloodType, that.bloodType) && Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodType, units);
    }

    @Override
    public String toString() {
        return "BloodUnitsCount{" +
                "bloodType='" + bloodType + '\'' +
                ", units=" + units +
                '}';
    }
}
