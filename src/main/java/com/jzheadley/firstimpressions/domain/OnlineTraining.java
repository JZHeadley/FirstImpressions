package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OnlineTraining.
 */
@Entity
@Table(name = "online_training")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "onlinetraining")
public class OnlineTraining implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_link")
    private String trainingLink;

    @Column(name = "jhi_desc")
    private String desc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingLink() {
        return trainingLink;
    }

    public OnlineTraining trainingLink(String trainingLink) {
        this.trainingLink = trainingLink;
        return this;
    }

    public void setTrainingLink(String trainingLink) {
        this.trainingLink = trainingLink;
    }

    public String getDesc() {
        return desc;
    }

    public OnlineTraining desc(String desc) {
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
        OnlineTraining onlineTraining = (OnlineTraining) o;
        if (onlineTraining.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), onlineTraining.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OnlineTraining{" +
            "id=" + getId() +
            ", trainingLink='" + getTrainingLink() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
