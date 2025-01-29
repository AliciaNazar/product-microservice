package com.mindhub.product_microservice.controllers;

import com.mindhub.product_microservice.dtos.ExistentProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTO;
import com.mindhub.product_microservice.dtos.ProductDTOResquest;
import com.mindhub.product_microservice.dtos.ProductQuantityDTO;
import com.mindhub.product_microservice.exceptions.CustomException;
import com.mindhub.product_microservice.models.Product;
import com.mindhub.product_microservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
    })
    @GetMapping()
    public ResponseEntity<Set<ProductDTO>> getProducts(){
        Set<ProductDTO> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws CustomException {
        try{
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            throw new CustomException("Product not found",HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new product", description = "Registers a new product in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
    })
    @PostMapping()
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productDTO = this.productService.createProduct(productDTOResquest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }


    @Operation(summary = "Delete a product by ID", description = "Deletes an existing product by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping
    public ResponseEntity<HashMap<Long, Integer>> existsProducts(@RequestBody List<ProductQuantityDTO> productQuantityDTOList){
        HashMap<Long, Integer> products = productService.getAllAvailableProducts(productQuantityDTOList);
        return ResponseEntity.ok(products);
    }

    @PutMapping("to-order")
    public ResponseEntity<String> existProduct(@RequestBody List<ProductQuantityDTO> productQuantityList) throws CustomException {

        productService.updateProductsQuantity(productQuantityList);
        return new ResponseEntity<String>("The update was successful", HttpStatus.OK);
    }


}

