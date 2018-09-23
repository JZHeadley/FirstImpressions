package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClothingCompany.
 */
@Entity
@Table(name = "clothing_company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clothingcompany")
public class ClothingCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_desc")
    private String companyDesc;

    @OneToOne
    @JoinColumn(unique = true)
    private Location companyLocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public ClothingCompany companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public ClothingCompany companyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
        return this;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public Location getCompanyLocation() {
        return companyLocation;
    }

    public ClothingCompany companyLocation(Location location) {
        this.companyLocation = location;
        return this;
    }

    public void setCompanyLocation(Location location) {
        this.companyLocation = location;
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
        ClothingCompany clothingCompany = (ClothingCompany) o;
        if (clothingCompany.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clothingCompany.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClothingCompany{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", companyDesc='" + getCompanyDesc() + "'" +
            "}";
    }
}
