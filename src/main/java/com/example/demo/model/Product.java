package com.example.demo.model;
import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String discountType = "NONE";
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detail_id", unique = true, nullable = false)
    private ProductDetail detail;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Review> reviews = new ArrayList<>();
    @Transient
    private Double discountedPrice;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public ProductDetail getDetail() { return detail; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    public Double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(Double discountedPrice) { this.discountedPrice = discountedPrice; }
    public void setDetail(ProductDetail detail) {
        if (this.detail != null) this.detail.setProduct(null);
        this.detail = detail;
        if (detail != null) detail.setProduct(this);
    }
    public void addReview(Review review) { reviews.add(review); review.setProduct(this); }
    public void removeReview(Review review) { reviews.remove(review); review.setProduct(null); }
}
