package com.jzheadley.firstimpressions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_title")
    private String programTitle;

    @Column(name = "program_link")
    private String programLink;

    @Column(name = "program_desc")
    private String programDesc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public Program programTitle(String programTitle) {
        this.programTitle = programTitle;
        return this;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getProgramLink() {
        return programLink;
    }

    public Program programLink(String programLink) {
        this.programLink = programLink;
        return this;
    }

    public void setProgramLink(String programLink) {
        this.programLink = programLink;
    }

    public String getProgramDesc() {
        return programDesc;
    }

    public Program programDesc(String programDesc) {
        this.programDesc = programDesc;
        return this;
    }

    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
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
        Program program = (Program) o;
        if (program.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), program.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", programTitle='" + getProgramTitle() + "'" +
            ", programLink='" + getProgramLink() + "'" +
            ", programDesc='" + getProgramDesc() + "'" +
            "}";
    }
}
