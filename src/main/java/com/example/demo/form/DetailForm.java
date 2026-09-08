package com.example.demo.form;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.*;

public class DetailForm {
    @Size(max = 4000)
    private String description;
    @Size(max = 255)
    private String warranty;
    @PositiveOrZero
    private Double weight;
    @Size(max = 255)
    private String dimensions;
    @Size(max = 255)
    private String manufacturedCountry;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWarranty() { return warranty; }
    public void setWarranty(String warranty) { this.warranty = warranty; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public String getManufacturedCountry() { return manufacturedCountry; }
    public void setManufacturedCountry(String manufacturedCountry) { this.manufacturedCountry = manufacturedCountry; }

}
