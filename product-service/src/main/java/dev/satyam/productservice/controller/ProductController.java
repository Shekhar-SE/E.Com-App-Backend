package dev.satyam.productservice.controller;

import dev.satyam.productservice.model.Product;
import dev.satyam.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductWithCreator(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            //Inter-service communication part
            RestTemplate restTemplate = new RestTemplate();

            //Call the user-service to get user details
            String userUrl = "http://localhost:8081/api/users/" + product.getCreatorId();
            ResponseEntity<Object> userResponse = restTemplate.getForEntity(userUrl, Object.class);

            //Combining the data and returning it
            Map<String, Object> response = new HashMap<>();
            response.put("product", product);
            response.put("creator", userResponse.getBody());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
