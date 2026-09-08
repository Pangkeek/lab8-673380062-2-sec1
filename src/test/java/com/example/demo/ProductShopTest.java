package com.example.demo;

import com.example.demo.repository.*;
import com.example.demo.strategy.DiscountContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductShopTest {
    @Autowired MockMvc mvc;
    @Autowired ProductRepository products;
    @Autowired ProductDetailRepository details;
    @Autowired ReviewRepository reviews;
    @Autowired DiscountContext discounts;

    @BeforeEach void clear() { products.deleteAll(); }

    private MockHttpServletRequestBuilder product(String path) {
        return post(path).param("name", "iPhone 15 Pro (673380062-2 SEC 1)")
            .param("category", "Electronics").param("brand", "Apple")
            .param("price", "40000").param("stock", "10").param("discountType", "MEMBER")
            .param("detail.description", "Test product").param("detail.warranty", "1 Year")
            .param("detail.weight", "0.19").param("detail.dimensions", "15x7x0.8 cm")
            .param("detail.manufacturedCountry", "China");
    }

    @Test void fullCrudPreservesRelationshipsAndDeletesChildren() throws Exception {
        mvc.perform(get("/products/add")).andExpect(status().isOk());
        mvc.perform(product("/products/save").param("reviews[0].reviewer", "First")
            .param("reviews[0].rating", "5").param("reviews[0].comment", "Excellent"))
            .andExpect(status().is3xxRedirection());
        var p = products.findAll().get(0);
        Long id = p.getId(), detailId = p.getDetail().getId();
        assertThat(p.getReviews()).hasSize(1);
        assertThat(reviews.findAll().get(0).getReviewDate()).isNotNull();
        mvc.perform(get("/products")).andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("36000.00")));
        mvc.perform(post("/products/"+id+"/reviews").param("reviewer", "Second").param("rating", "4"))
            .andExpect(status().is3xxRedirection());
        mvc.perform(get("/products/"+id+"/reviews")).andExpect(status().isOk());
        mvc.perform(get("/products/edit/"+id)).andExpect(status().isOk());
        mvc.perform(product("/products/update/"+id).param("detail.id", "99999").param("id", "99999"))
            .andExpect(status().is3xxRedirection());
        p = products.findById(id).orElseThrow();
        assertThat(p.getDetail().getId()).isEqualTo(detailId);
        assertThat(p.getReviews()).hasSize(2);
        assertThat(details.count()).isEqualTo(1);
        Long reviewId = p.getReviews().get(0).getId();
        mvc.perform(post("/products/"+id+"/reviews/"+reviewId+"/delete"))
            .andExpect(status().is3xxRedirection());
        assertThat(reviews.count()).isEqualTo(1);
        mvc.perform(get("/products/delete/"+id)).andExpect(status().isOk());
        mvc.perform(post("/products/delete/"+id)).andExpect(status().is3xxRedirection());
        assertThat(products.count()).isZero();
        assertThat(details.count()).isZero();
        assertThat(reviews.count()).isZero();
    }

    @Test void emptyFirstReviewIsNotPersisted() throws Exception {
        mvc.perform(product("/products/save")).andExpect(status().is3xxRedirection());
        assertThat(products.count()).isEqualTo(1);
        assertThat(reviews.count()).isZero();
    }

    @Test void invalidProductAndPartialReviewAreRejected() throws Exception {
        mvc.perform(post("/products/save").param("name", " ").param("price", "-1"))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("product"));
        mvc.perform(product("/products/save").param("reviews[0].comment", "Missing reviewer"))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("product"));
        assertThat(products.count()).isZero();
    }

    @Test void invalidRatingIsRejectedAndMissingProductIs404() throws Exception {
        mvc.perform(product("/products/save")).andExpect(status().is3xxRedirection());
        Long id = products.findAll().get(0).getId();
        mvc.perform(post("/products/"+id+"/reviews").param("reviewer", "A").param("rating", "6"))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("review"));
        assertThat(reviews.count()).isZero();
        mvc.perform(get("/products/edit/999999")).andExpect(status().isNotFound());
    }

    @Test void reviewCannotBeDeletedThroughAnotherProduct() throws Exception {
        mvc.perform(product("/products/save").param("reviews[0].reviewer", "Owner"));
        Long reviewId = reviews.findAll().get(0).getId();
        mvc.perform(product("/products/save"));
        Long otherId = products.findAll().stream().filter(p -> p.getReviews().isEmpty()).findFirst().orElseThrow().getId();
        mvc.perform(post("/products/"+otherId+"/reviews/"+reviewId+"/delete")).andExpect(status().isNotFound());
        assertThat(reviews.count()).isEqualTo(1);
    }

    @Test void allDiscountStrategiesAndRounding() {
        assertThat(discounts.calculate("NONE", 100)).isEqualTo(100);
        assertThat(discounts.calculate("MEMBER", 100)).isEqualTo(90);
        assertThat(discounts.calculate("SEASONAL", 100)).isEqualTo(80);
        assertThat(discounts.calculate("MEMBER", 19.99)).isEqualTo(17.99);
        assertThatThrownBy(() -> discounts.calculate("INVALID", 100)).isInstanceOf(IllegalArgumentException.class);
    }
}
