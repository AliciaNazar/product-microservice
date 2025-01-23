package com.mindhub.product_microservice.controllers;

import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all products", description = "Retrieve a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = this.productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create a new product", description = "Registers a new product in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
    })
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productDTO = this.productService.createProduct(productDTOResquest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @Operation(summary = "Update the stock of a product.", description = "Updates the stock of a specific product identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProductStock(
            @PathVariable("id") Long id,
            @RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productUpdated = this.productService.updateProductStock(id, productDTOResquest);
        return ResponseEntity.ok(productUpdated);
    }

    @Operation(summary = "Delete a product by ID", description = "Deletes an existing product by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @DeleteMapping("products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

