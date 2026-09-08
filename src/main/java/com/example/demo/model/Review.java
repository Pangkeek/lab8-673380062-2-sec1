package com.example.demo.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import org.hibernate.annotations.Check;
@Entity
@Table(name = "reviews")
@Check(constraints = "rating between 1 and 5")
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String reviewer;
    @Column(nullable = false)
    private Integer rating;
    @Column(length = 2000)
    private String comment;
    @Column(nullable = false)
    private LocalDate reviewDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

}
