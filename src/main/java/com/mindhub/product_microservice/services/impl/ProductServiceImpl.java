package com.mindhub.product_microservice.services.impl;

import com.mindhub.product_microservice.dtos.ExistentProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.dtos.ProductQuantityDTO;
import com.mindhub.product_microservice.exceptions.CustomException;
import com.mindhub.product_microservice.models.Product;
import com.mindhub.product_microservice.repositories.ProductRepository;
import com.mindhub.product_microservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Set<ProductDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        Set<ProductDTO> productDTOs = productList.stream()
                .map(product -> new ProductDTO(product))
                .collect(Collectors.toSet());
        return productDTOs;
    }

    @Override
    public Product getProductById(Long id) throws CustomException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Product not found.", HttpStatus.NOT_FOUND));
        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public ExistentProductDTO updateProduct(Long id, ProductDTOResquest productDTOResquest) throws CustomException {
        inputProductValidations(productDTOResquest);
        Product product = productRepository.findById(id)
                .orElseThrow(()->new CustomException("Product not found.", HttpStatus.NOT_FOUND));
        product.setDescription(productDTOResquest.getDescription());
        product.setPrice(productDTOResquest.getPrice());
        product.setStock(productDTOResquest.getStock());
        product.setName(productDTOResquest.getName());
        product = productRepository.save(product);

        return new ExistentProductDTO(product.getId(),product.getPrice(), product.getStock());
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

    @Override
    public boolean existsProductById(Long id)  {
        boolean exists = productRepository.existsById(id);
        if (!exists){
            throw new CustomException("Product doesn't exist",HttpStatus.NOT_FOUND);
        }
        return exists;
    }

    @Override
    public HashMap<Long, Integer> getAllAvailableProducts(List<ProductQuantityDTO> productQuantityList){
        HashMap<Long, Integer> availableProductMap = new HashMap<>();

        productQuantityList.forEach( product -> {
            try{
                Product aux = getProductById(product.getId());
                availableProductMap.put(aux.getId(), aux.getStock());
            }catch (Exception e){}
        });
        return availableProductMap;
    }

    @Override
    public ExistentProductDTO getOneAvailableProduct(ProductQuantityDTO productQuantity){
        try {
            Product product = getProductById(productQuantity.getId());
            if (product.getStock()>= productQuantity.getQuantity()){
                product.setStock(product.getStock()-productQuantity.getQuantity());
                productRepository.save(product);
                return new ExistentProductDTO(product.getId(), product.getPrice(), productQuantity.getQuantity());
            }else{
                return new ExistentProductDTO(product.getId(), null, productQuantity.getQuantity());
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateProductsQuantity(List<ProductQuantityDTO> productQuantityList){
        productQuantityList.forEach(product ->{
            try {
                updateProductQuantity(product.getId(), product.getQuantity());
            } catch (Exception e) {}
        });
    }

    @Override
    public void updateProductQuantity(Long idProduct, Integer quantity) throws CustomException {
        Product product = getProductById(idProduct);
        if (product.getStock()+quantity<0){
            throw new CustomException("Not enough stock", HttpStatus.NOT_ACCEPTABLE);
        }
        product.setStock(product.getStock()+quantity);
        productRepository.save(product);

    }

    @Override
    public boolean existsProductByName(String name) {
        return productRepository.existsByName(name);
    }


}










