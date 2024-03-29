package com.picture.shop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "picture")
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", columnDefinition = "TEXT")
    @NotNull
    @NotBlank
    private String name;
    @Column(name = "price")
    @NotNull
    private Integer price;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    @NotNull
    @NotBlank
    private String description;
    @Column(name = "piture_url", columnDefinition = "LONGTEXT")
    @NotNull
    @NotBlank
    private String pictureUrl;

    public Picture(String name, Integer price, String description, String pictureUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
