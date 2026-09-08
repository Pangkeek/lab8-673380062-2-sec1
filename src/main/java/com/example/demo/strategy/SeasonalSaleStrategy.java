package com.example.demo.strategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
@Component
public class SeasonalSaleStrategy implements DiscountStrategy {
    public String type() { return "SEASONAL"; }
    public double apply(double price) {
        return BigDecimal.valueOf(price).multiply(new BigDecimal("0.80"))
            .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
