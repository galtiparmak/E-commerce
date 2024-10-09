package com.geko.ecommerce.Controller;

import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductAverage;
import com.geko.ecommerce.Entity.ProductDocument;
import com.geko.ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveProduct(@RequestBody Product product) {
        if (productService.createProduct(product)) {
            return ResponseEntity.ok("Product saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Product not saved");
        }
    } // http://localhost:8080/api/product/save

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Product not deleted");
        }
    } // http://localhost:8080/api/product/delete?id=1

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    } // http://localhost:8080/api/product/getAll

    @GetMapping("/getAllDocuments")
    public ResponseEntity<Iterable<ProductDocument>> getAllDocuments() {
        return ResponseEntity.ok(productService.getAllDocuments());
    } // http://localhost:8080/api/product/getAllDocuments

    @PostMapping("/reviewProduct")
    public ResponseEntity<String> reviewProduct(@RequestParam String username, @RequestParam String productName, @RequestParam String review, @RequestParam int rating) {
        if (productService.reviewProduct(username, productName, review, rating)) {
            return ResponseEntity.ok("Product reviewed successfully");
        } else {
            return ResponseEntity.badRequest().body("Product not reviewed");
        }
    } // http://localhost:8080/api/product/reviewProduct?userId=1&productName=product1&review=good&rating=5

    @GetMapping("/getTopRatedProducts")
    public ResponseEntity<List<ProductAverage>> getTopRatedProducts(@RequestParam int k) {
        return ResponseEntity.ok(productService.getTopKRatedProducts(k));
    } // http://localhost:8080/api/product/getTopRatedProducts?k=5

    @GetMapping("/getBottomRatedProducts")
    public ResponseEntity<List<ProductAverage>> getBottomKRatedProducts(@RequestParam int k) {
        return ResponseEntity.ok(productService.getBottomKRatedProducts(k));
    } // http://localhost:8080/api/product/getBottomRatedProducts?k=5
}
