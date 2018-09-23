package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InterviewResources.
 */
@Entity
@Table(name = "interview_resources")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "interviewresources")
public class InterviewResources implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_link")
    private String resourceLink;

    @Column(name = "jhi_desc")
    private String desc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceLink() {
        return resourceLink;
    }

    public InterviewResources resourceLink(String resourceLink) {
        this.resourceLink = resourceLink;
        return this;
    }

    public void setResourceLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getDesc() {
        return desc;
    }

    public InterviewResources desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        InterviewResources interviewResources = (InterviewResources) o;
        if (interviewResources.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interviewResources.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InterviewResources{" +
            "id=" + getId() +
            ", resourceLink='" + getResourceLink() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
