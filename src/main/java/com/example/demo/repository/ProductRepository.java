package com.example.demo.repository;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detail", "reviews"})
    java.util.List<Product> findAll();
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detail", "reviews"})
    java.util.Optional<Product> findById(Long id);
}
