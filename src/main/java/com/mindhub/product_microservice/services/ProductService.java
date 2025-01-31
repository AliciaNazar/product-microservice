package com.mindhub.product_microservice.services;

import com.mindhub.product_microservice.dtos.ExistentProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.dtos.ProductQuantityDTO;
import com.mindhub.product_microservice.exceptions.CustomException;
import com.mindhub.product_microservice.models.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface ProductService {


    Set<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTOResquest productDTOResquest);
//    ProductDTO updateProduct(Long id, ProductDTOResquest productDTOResquest);
    ProductDTO updateProductStock(Long id, ProductDTOResquest productDTOResquest);
    void deleteProduct(Long id);
    Product getProductById(Long id) throws CustomException;
    boolean existsProductById(Long id);
    HashMap<Long, Integer> getAllAvailableProducts(List<ProductQuantityDTO> productQuantityList);
    ExistentProductDTO getOneAvailableProduct(ProductQuantityDTO productQuantity);
    void updateProductQuantity(Long idProduct, Integer quantity) throws CustomException;
    void updateProductsQuantity(List<ProductQuantityDTO> productQuantityList);
    boolean existsProductByName(String name);

    ExistentProductDTO updateProduct(Long id, ProductDTOResquest productDTOResquest) throws CustomException;
}
