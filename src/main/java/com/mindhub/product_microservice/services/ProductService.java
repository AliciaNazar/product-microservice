package com.mindhub.product_microservice.services;

import com.mindhub.product_microservice.dtos.ExistentProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.dtos.ProductQuantityDTO;
import com.mindhub.product_microservice.models.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProducts();
    ProductDTO createProduct(ProductDTOResquest productDTOResquest);
    ProductDTO updateProduct(Long id, ProductDTOResquest productDTOResquest);
    ProductDTO updateProductStock(Long id, ProductDTOResquest productDTOResquest);
    void deleteProduct(Long id);


    Product getProductById(Long id) throws Exception;
    boolean existsProductById(Long id);
    List<ExistentProductDTO> getAllAvailableProducts(List<ProductQuantityDTO> productQuantityList);
}
