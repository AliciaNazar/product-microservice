package com.mindhub.product_microservice.controllers;

import com.mindhub.product_microservice.config.JwtUtils;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
    })
    @GetMapping("/public")
    public ResponseEntity<Set<ProductDTO>> getProducts(){
        Set<ProductDTO> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @GetMapping("/admin/{id}")
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
    @PostMapping("/admin")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTOResquest productDTOResquest){
        ProductDTO productDTO = this.productService.createProduct(productDTOResquest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ExistentProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTOResquest productDTOResquest) throws CustomException {
        ExistentProductDTO updatedProduct = productService.updateProduct(id, productDTOResquest);
        return ResponseEntity.ok(updatedProduct);
    }


    @Operation(summary = "Delete a product by ID", description = "Deletes an existing product by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }









    @Operation(summary = "Check available products", description = "Checks the availability of a list of products based on a list with the required quantity and returns a collection with those products and their stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request body.")
    })
    @PutMapping()
    public ResponseEntity<HashMap<Long, Integer>> existsProducts(@RequestBody List<ProductQuantityDTO> productQuantityDTOList){
        HashMap<Long, Integer> products = productService.getAllAvailableProducts(productQuantityDTOList);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update product stock for order", description = "Updates the stock of products when an order is placed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data."),
            @ApiResponse(responseCode = "404", description = "One or more products not found.")
    })
    @PutMapping("to-order")
    public ResponseEntity<String> existProduct(@RequestBody List<ProductQuantityDTO> productQuantityList) throws CustomException {

        productService.updateProductsQuantity(productQuantityList);
        return new ResponseEntity<String>("The update was successful", HttpStatus.OK);
    }

}

