package com.example.demo.controller;
import com.example.demo.form.*;
import com.example.demo.service.ProductService;
import com.example.demo.strategy.DiscountContext;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class ProductController {
    private final ProductService service;
    private final DiscountContext discounts;
    public ProductController(ProductService service, DiscountContext discounts) {
        this.service = service; this.discounts = discounts;
    }
    @InitBinder("product")
    void bindProduct(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(1);
        binder.setAllowedFields("name", "category", "brand", "stock", "price", "discountType",
            "detail.description", "detail.warranty", "detail.weight", "detail.dimensions",
            "detail.manufacturedCountry", "reviews[0].reviewer", "reviews[0].rating", "reviews[0].comment");
    }
    @GetMapping("/") public String home() { return "redirect:/products"; }
    @GetMapping("/products")
    public String list(Model model) { model.addAttribute("products", service.findAll()); return "products/list"; }
    @GetMapping("/products/add")
    public String add(Model model) { model.addAttribute("product", new ProductForm()); return "products/add"; }
    @PostMapping("/products/save")
    public String save(@Valid @ModelAttribute("product") ProductForm form, BindingResult errors, RedirectAttributes flash) {
        validateDiscount(form, errors);
        if (errors.hasErrors()) return "products/add";
        service.create(form); flash.addFlashAttribute("message", "เพิ่มสินค้าเรียบร้อยแล้ว");
        return "redirect:/products";
    }
    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.editForm(id)); return "products/edit";
    }
    @PostMapping("/products/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("product") ProductForm form,
                         BindingResult errors, RedirectAttributes flash) {
        service.findById(id); form.setId(id); validateDiscount(form, errors);
        if (errors.hasErrors()) return "products/edit";
        service.update(id, form); flash.addFlashAttribute("message", "แก้ไขสินค้าเรียบร้อยแล้ว");
        return "redirect:/products";
    }
    @GetMapping("/products/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.findById(id)); return "products/delete";
    }
    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        service.delete(id); flash.addFlashAttribute("message", "ลบสินค้า ข้อมูลเสริม และรีวิวเรียบร้อยแล้ว");
        return "redirect:/products";
    }
    @GetMapping("/products/{id}/reviews")
    public String reviews(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.findById(id));
        model.addAttribute("review", new ReviewForm()); return "products/reviews";
    }
    @PostMapping("/products/{id}/reviews")
    public String addReview(@PathVariable Long id, @Valid @ModelAttribute("review") ReviewForm form,
                            BindingResult errors, Model model) {
        model.addAttribute("product", service.findById(id));
        if (form.getReviewer() == null || form.getReviewer().isBlank())
            errors.rejectValue("reviewer", "required", "กรุณากรอกชื่อผู้รีวิว");
        if (errors.hasErrors()) return "products/reviews";
        service.addReview(id, form); return "redirect:/products/" + id + "/reviews";
    }
    @PostMapping("/products/{id}/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
        service.deleteReview(id, reviewId); return "redirect:/products/" + id + "/reviews";
    }
    private void validateDiscount(ProductForm f, BindingResult errors) {
        if (!discounts.supports(f.getDiscountType())) errors.rejectValue("discountType", "invalid", "ส่วนลดไม่ถูกต้อง");
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() { return "error/404"; }
}
