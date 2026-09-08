package com.example.demo.strategy;
public interface DiscountStrategy {
    String type();
    double apply(double price);
}
