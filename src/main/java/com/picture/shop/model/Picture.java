package com.picture.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "picture")
@NoArgsConstructor
@Getter
@Setter
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
    @NotBlank
    private Integer price;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    @NotNull
    @NotBlank
    private String description;

    public Picture(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
