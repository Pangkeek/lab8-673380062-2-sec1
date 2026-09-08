package com.example.demo.form;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.*;

public class ProductForm {
    @NotBlank @Size(max = 255)
    private String name;
    @NotBlank @Size(max = 255)
    private String category;
    @NotBlank @Size(max = 255)
    private String brand;
    @NotNull @PositiveOrZero
    private Integer stock;
    @NotNull @PositiveOrZero
    private Double price;
    @NotBlank @Size(max = 255)
    private String discountType = "NONE";
    
    private Long id;
    @Valid @NotNull
    private DetailForm detail = new DetailForm();
    @Valid @Size(max = 1)
    private List<ReviewForm> reviews = new ArrayList<>(List.of(new ReviewForm()));
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DetailForm getDetail() { return detail; }
    public void setDetail(DetailForm detail) { this.detail = detail; }
    public List<ReviewForm> getReviews() { return reviews; }
    public void setReviews(List<ReviewForm> reviews) { this.reviews = reviews; }

}
