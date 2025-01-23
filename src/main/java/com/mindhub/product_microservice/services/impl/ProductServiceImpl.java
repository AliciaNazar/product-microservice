package com.mindhub.product_microservice.services.impl;

import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.exceptions.CustomException;
import com.mindhub.product_microservice.models.Product;
import com.mindhub.product_microservice.repositories.ProductRepository;
import com.mindhub.product_microservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product))
                .toList();
        return productDTOs;
    }

    @Override
    public ProductDTO createProduct(ProductDTOResquest productDTOResquest) {
        inputProductValidations(productDTOResquest);

        Product product = new Product();
        product.setName(productDTOResquest.getName());
        product.setDescription(productDTOResquest.getDescription());
        product.setPrice(productDTOResquest.getPrice());
        product.setStock(productDTOResquest.getStock());
        product = this.productRepository.save(product);
        return new ProductDTO(product);
    }

    @Override
    public ProductDTO updateProduct (Long id, ProductDTOResquest productDTOResquest){
        inputProductValidations(productDTOResquest);

        Product product = this.productRepository.findById(id)
                .orElseThrow();
        product.setName(productDTOResquest.getName());
        product.setDescription(productDTOResquest.getDescription());
        product.setStock(productDTOResquest.getStock());
        product.setPrice(productDTOResquest.getPrice());
        product = this.productRepository.save(product);
        return new ProductDTO(product);
    }


    @Override
    public ProductDTO updateProductStock (Long id, ProductDTOResquest productDTOResquest){
        stockValidations(productDTOResquest.getStock());
        idValidation(id);
        if(this.productRepository.existsById(id)){
            Product product = this.productRepository.findById(id)
                    .orElseThrow();
            product.setStock(productDTOResquest.getStock());
            product = this.productRepository.save(product);
            return new ProductDTO(product);
        }else{
            throw new CustomException("Product not found.", HttpStatus.NOT_FOUND);
        }


    }


    @Override
    public void deleteProduct(Long id){
        idValidation(id);
        if(this.productRepository.existsById(id)){
            this.productRepository.deleteById(id);
        }else{
            throw new CustomException("Product not found.", HttpStatus.NOT_FOUND);
        }



    }


    private void inputProductValidations(ProductDTOResquest productDTOResquest){
        productNameValidations(productDTOResquest.getName());
        productDescriptionValidations(productDTOResquest.getDescription());
        stockValidations(productDTOResquest.getStock());
        priceValidations(productDTOResquest.getPrice());
    }

    private void productNameValidations(String name){
        if (name.isBlank()){
            throw new CustomException("Product name can't be empty.");
        }
    }

    private void productDescriptionValidations(String description){
        if (description.isBlank()){
            throw new CustomException("Product description can't be empty.");
        }
    }

    private void stockValidations(Integer stock){
        if (stock == null){
            throw new CustomException("Stock can't be null.");
        }
        if (stock < 0){
            throw new CustomException("Stock can't be negative");
        }
    }

    private void priceValidations(Double price){
        if (price == null){
            throw new CustomException("Price can't be null.");
        }
        if (price < 0){
            throw new CustomException("Price can't be negative");
        }
    }

    private void idValidation(Long id){
        if (id == null || id <= 0){
            throw new CustomException("Invalid id.");
        }
    }
}










