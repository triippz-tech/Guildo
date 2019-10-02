package com.triippztech.guildo.domain;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Page.
 */
@Entity
@Table(name = "page")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Size(max = 50)
    @Column(name = "slug", length = 50, nullable = false)
    private String slug;

    @NotNull
    @Column(name = "published", nullable = false)
    private Instant published;

    @Column(name = "edited")
    private Instant edited;

    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "body", nullable = false)
    private String body;

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

    public Page title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public Page slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Instant getPublished() {
        return published;
    }

    public Page published(Instant published) {
        this.published = published;
        return this;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    public Instant getEdited() {
        return edited;
    }

    public Page edited(Instant edited) {
        this.edited = edited;
        return this;
    }

    public void setEdited(Instant edited) {
        this.edited = edited;
    }

    public String getBody() {
        return body;
    }

    public Page body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Page)) {
            return false;
        }
        return id != null && id.equals(((Page) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Page{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", published='" + getPublished() + "'" +
            ", edited='" + getEdited() + "'" +
            ", body='" + getBody() + "'" +
            "}";
    }
}
