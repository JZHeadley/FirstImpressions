package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_link")
    private String jobLink;

    @Column(name = "job_desc")
    private String jobDesc;

    @Column(name = "company")
    private String company;

    @Column(name = "city")
    private String city;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Job jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLink() {
        return jobLink;
    }

    public Job jobLink(String jobLink) {
        this.jobLink = jobLink;
        return this;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public Job jobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
        return this;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getCompany() {
        return company;
    }

    public Job company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public Job city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
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
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", jobTitle='" + getJobTitle() + "'" +
            ", jobLink='" + getJobLink() + "'" +
            ", jobDesc='" + getJobDesc() + "'" +
            ", company='" + getCompany() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
