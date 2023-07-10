package com.loiane.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

@SQLDelete(sql = "UPDATE Course SET status = 'Inactive' WHERE id=?")
@Where(clause = "status <> 'Inactive'")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 200)
    @Column(length = 200, nullable = false)
    private String name;

    @Length(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull
    @Column(length = 10, nullable = false)
    private String category;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    @NotNull
    @Column(length = 8, nullable = false)
    private String status = "Active";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull String getCategory() {
        return category;
    }

    public void setCategory(@NotNull String category) {
        this.category = category;
    }

    public @NotNull Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull Double price) {
        this.price = price;
    }

    public @NotNull String getStatus() {
        return status;
    }

    public void setStatus(@NotNull String status) {
        this.status = status;
    }
}
