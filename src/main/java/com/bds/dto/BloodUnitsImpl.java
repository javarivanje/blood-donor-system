package com.bds.dto;

import com.bds.models.BloodType;

import java.util.Objects;

public class BloodUnitsImpl implements BloodUnits {

    private String bloodType;
    private Integer units;

    public BloodUnitsImpl() {
    }

    public BloodUnitsImpl(String bloodType, Integer units) {
        this.bloodType = bloodType;
        this.units = units;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType.toString();
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    @Override
    public BloodType getBloodType() {
        return BloodType.valueOf(this.bloodType);
    }

    @Override
    public Integer getTotalUnits() {
        return this.units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodUnitsImpl that = (BloodUnitsImpl) o;
        return bloodType == that.bloodType && Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodType, units);
    }

    @Override
    public String toString() {
        return "BloodUnitsImpl{" +
                "bloodType=" + bloodType +
                ", units=" + units +
                '}';
    }
}
