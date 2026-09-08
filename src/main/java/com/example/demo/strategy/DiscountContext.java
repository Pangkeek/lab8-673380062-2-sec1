package com.example.demo.strategy;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Component
public class DiscountContext {
    private final Map<String, DiscountStrategy> strategies;
    public DiscountContext(List<DiscountStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toUnmodifiableMap(DiscountStrategy::type, Function.identity()));
    }
    public boolean supports(String type) { return type != null && strategies.containsKey(type); }
    public double calculate(String type, double price) {
        if (!supports(type)) throw new IllegalArgumentException("Unknown discount type");
        return strategies.get(type).apply(price);
    }
}
