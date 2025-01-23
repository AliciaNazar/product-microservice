package com.mindhub.product_microservice.dtos;

import com.mindhub.product_microservice.models.Product;

public class ProductDTOResquest {

    private String name;
    private String description;
    private Double price;
    private Integer stock;

    public ProductDTOResquest() {
    }

    public ProductDTOResquest(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
