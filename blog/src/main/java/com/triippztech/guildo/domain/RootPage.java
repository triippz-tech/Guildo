package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A RootPage.
 */
@Entity
@Table(name = "root_page")
public class RootPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "title", length = 10, nullable = false)
    private String title;

    @NotNull
    @Size(max = 50)
    @Column(name = "slug", length = 50, nullable = false)
    private String slug;

    @ManyToOne
    @JsonIgnoreProperties("rootPages")
    private Page childPages;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public RootPage title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public RootPage slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Page getChildPages() {
        return childPages;
    }

    public RootPage childPages(Page page) {
        this.childPages = page;
        return this;
    }

    public void setChildPages(Page page) {
        this.childPages = page;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RootPage)) {
            return false;
        }
        return id != null && id.equals(((RootPage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RootPage{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            "}";
    }
}
