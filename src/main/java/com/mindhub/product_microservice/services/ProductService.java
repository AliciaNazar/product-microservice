package com.mindhub.product_microservice.services;

import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProducts();
    ProductDTO createProduct(ProductDTOResquest productDTOResquest);
    ProductDTO updateProduct(Long id, ProductDTOResquest productDTOResquest);
    ProductDTO updateProductStock(Long id, ProductDTOResquest productDTOResquest);
    void deleteProduct(Long id);
}
