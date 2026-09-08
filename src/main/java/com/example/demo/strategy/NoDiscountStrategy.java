package com.example.demo.strategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
@Component
public class NoDiscountStrategy implements DiscountStrategy {
    public String type() { return "NONE"; }
    public double apply(double price) {
        return BigDecimal.valueOf(price).multiply(new BigDecimal("1.00"))
            .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
