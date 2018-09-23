package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_number")
    private Integer buildingNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private Integer zipCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    public Location buildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
        return this;
    }

    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public Location streetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public Location state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public Location zipCode(Integer zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", buildingNumber=" + getBuildingNumber() +
            ", streetName='" + getStreetName() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode=" + getZipCode() +
            "}";
    }
}
