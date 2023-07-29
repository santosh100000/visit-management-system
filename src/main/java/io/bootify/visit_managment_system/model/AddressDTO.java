package io.bootify.visit_managment_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class AddressDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String line1;

    @NotNull
    @Size(max = 255)
    private String line2;

    private Integer pincode;

    @NotNull
    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(final String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(final String line2) {
        this.line2 = line2;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(final Integer pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

}
