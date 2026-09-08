package com.example.demo.service;
import com.example.demo.model.*;
import com.example.demo.form.*;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class ProductService {
    private final ProductRepository products;
    private final DiscountContext discounts;
    public ProductService(ProductRepository products, DiscountContext discounts) {
        this.products = products;
        this.discounts = discounts;
    }
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        List<Product> result = products.findAll();
        result.forEach(p -> p.setDiscountedPrice(discounts.calculate(p.getDiscountType(), p.getPrice())));
        return result;
    }
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return products.findById(id).orElseThrow(() -> new NoSuchElementException("ไม่พบสินค้า"));
    }
    public Product create(ProductForm form) {
        Product product = new Product();
        product.setDetail(new ProductDetail());
        copy(form, product);
        for (ReviewForm r : form.getReviews()) {
            if (r.hasContent()) product.addReview(toReview(r));
        }
        return products.save(product);
    }
    public Product update(Long id, ProductForm form) {
        Product product = findById(id);
        copy(form, product); // Update the existing detail; preserve its ID and all reviews.
        return products.save(product);
    }
    public void delete(Long id) { products.delete(findById(id)); }
    public void addReview(Long id, ReviewForm form) {
        Product product = findById(id);
        product.addReview(toReview(form));
        products.save(product);
    }
    public void deleteReview(Long id, Long reviewId) {
        Product product = findById(id);
        Review review = product.getReviews().stream().filter(r -> r.getId().equals(reviewId))
            .findFirst().orElseThrow(() -> new NoSuchElementException("ไม่พบรีวิวของสินค้านี้"));
        product.removeReview(review);
    }
    private Review toReview(ReviewForm form) {
        if (form.getReviewer() == null || form.getReviewer().isBlank()
            || form.getRating() == null || form.getRating() < 1 || form.getRating() > 5)
            throw new IllegalArgumentException("Invalid review");
        Review review = new Review();
        review.setReviewer(form.getReviewer().trim());
        review.setRating(form.getRating()); review.setComment(form.getComment());
        review.setReviewDate(LocalDate.now());
        return review;
    }
    private void copy(ProductForm f, Product p) {
        if (!discounts.supports(f.getDiscountType())) throw new IllegalArgumentException("Unknown discount type");
        p.setName(f.getName().trim()); p.setCategory(f.getCategory().trim());
        p.setBrand(f.getBrand().trim()); p.setPrice(f.getPrice());
        p.setStock(f.getStock()); p.setDiscountType(f.getDiscountType());
        DetailForm d = f.getDetail();
        p.getDetail().setDescription(d.getDescription());
        p.getDetail().setWarranty(d.getWarranty()); p.getDetail().setWeight(d.getWeight());
        p.getDetail().setDimensions(d.getDimensions());
        p.getDetail().setManufacturedCountry(d.getManufacturedCountry());
    }
    @Transactional(readOnly = true)
    public ProductForm editForm(Long id) {
        Product p = findById(id); ProductForm f = new ProductForm();
        f.setId(p.getId()); f.setName(p.getName()); f.setCategory(p.getCategory());
        f.setBrand(p.getBrand()); f.setPrice(p.getPrice()); f.setStock(p.getStock());
        f.setDiscountType(p.getDiscountType());
        ProductDetail d = p.getDetail();
        f.getDetail().setDescription(d.getDescription()); f.getDetail().setWarranty(d.getWarranty());
        f.getDetail().setWeight(d.getWeight()); f.getDetail().setDimensions(d.getDimensions());
        f.getDetail().setManufacturedCountry(d.getManufacturedCountry());
        return f;
    }
}
