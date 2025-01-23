package com.mindhub.product_microservice.controllers;

import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = this.productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productDTO = this.productService.createProduct(productDTOResquest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProductStock(
            @PathVariable("id") Long id,
            @RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productUpdated = this.productService.updateProductStock(id, productDTOResquest);
        return ResponseEntity.ok(productUpdated);
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

