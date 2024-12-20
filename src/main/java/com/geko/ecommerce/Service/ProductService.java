package com.geko.ecommerce.Service;

import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.DTO.Product.ProductMapper;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductAverage;
import com.geko.ecommerce.Entity.ProductDocument;
import com.geko.ecommerce.Entity.ProductReview;
import com.geko.ecommerce.Producer.ProductNodeProducer;
import com.geko.ecommerce.Producer.ProductProducer;
import com.geko.ecommerce.Producer.ProductReviewProducer;
import com.geko.ecommerce.Repository.elasticsearch.ProductSearchRepository;
import com.geko.ecommerce.Repository.mongodb.ProductReviewRepository;
import com.geko.ecommerce.Repository.mysql.ProductAverageRepository;
import com.geko.ecommerce.Repository.mysql.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductProducer productProducer;
    private final ProductAverageRepository productAverageRepository;
    private final ProductReviewProducer productReviewProducer;
    private final ProductSearchRepository productSearchRepository;
    private final ProductNodeProducer productNodeProducer;
    private final ProductReviewRepository productReviewRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository
            ,ProductProducer productProducer
            ,ProductAverageRepository productAverageRepository
            ,ProductReviewProducer productReviewProducer
            ,ProductSearchRepository productSearchRepository, ProductNodeProducer productNodeProducer
            , ProductReviewRepository productReviewRepository) {
        this.productRepository = productRepository;
        this.productProducer = productProducer;
        this.productAverageRepository = productAverageRepository;
        this.productReviewProducer = productReviewProducer;
        this.productSearchRepository = productSearchRepository;
        this.productNodeProducer = productNodeProducer;
        this.productReviewRepository = productReviewRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(ProductMapper.toDTO(product));
        }
        return productDTOS;
    }

    public Iterable<ProductDocument> getAllDocuments() {
        return productSearchRepository.findAll();
    }

    public ProductDTO getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        return ProductMapper.toDTO(optionalProduct.get());
    }

    public ProductDTO getProductByName(String name) {
        Optional<Product> optionalProduct = productRepository.findByName(name);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        return ProductMapper.toDTO(optionalProduct.get());
    }

    public boolean create(Product product) {
        if (product != null) {
            try {
                createProduct(product);
                createProductNode(product);
                return true;
            } catch (RuntimeException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
        return false;
    }

    @Transactional("transactionManager")
    public void createProduct(Product product) {
        productProducer.createProduct(product);
    }

    @Transactional("neo4jTransactionManager")
    public void createProductNode(Product product) {
        productNodeProducer.create(product);
    }

    public boolean deleteProduct(String name) {
        try {
            Optional<Product> optionalProduct = productRepository.findByName(name);
            if (optionalProduct.isEmpty()) {
                throw new RuntimeException("Product not found with name: " + name);
            }
            Product product = optionalProduct.get();

            productProducer.deleteProduct(product);
            return true;
        } catch (RuntimeException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public ProductReview reviewProduct(String username, String productName, String review, int rating) {
        try {
            ProductReview productReview = new ProductReview();
            productReview.setProductName(productName);
            productReview.setUsername(username);
            productReview.setReviewText(review);
            productReview.setRating(rating);
            System.out.println("Product review created: " + productReview);
            productReviewProducer.createReview(productReview);
            System.out.println("Product review sent to Kafka: " + productReview);
            return productReview;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProductReview> getReview(String name) {
        return productReviewRepository.findByProductName(name);
    }

    public List<ProductAverage> getTopKRatedProducts(int k) {
        PriorityQueue<ProductAverage> minHeap = new PriorityQueue<>();
        List<ProductAverage> productAverages = productAverageRepository.findAll();

        if (productAverages.size() < k) {
            throw new RuntimeException("Not enough products to get top " + k + " rated products");
        }

        int i = 0;
        for (i = 0; i < k; i++) {
            minHeap.add(productAverages.get(i));
        }

        for (; i < productAverages.size(); i++) {
            if (!minHeap.isEmpty() && productAverages.get(i).getAverage() > minHeap.peek().getAverage()) {
                minHeap.poll();
                minHeap.add(productAverages.get(i));
            }
        }

        return new ArrayList<>(minHeap);
    }

    public List<ProductAverage> getBottomKRatedProducts(int k) {
        PriorityQueue<ProductAverage> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        List<ProductAverage> productAverages = productAverageRepository.findAll();

        if (productAverages.size() < k) {
            throw new RuntimeException("Not enough products to get bottom " + k + " rated products");
        }

        int i = 0;
        for (i = 0; i < k; i++) {
            maxHeap.add(productAverages.get(i));
        }

        for (; i < productAverages.size(); i++) {
            if (!maxHeap.isEmpty() && productAverages.get(i).getAverage() < maxHeap.peek().getAverage()) {
                maxHeap.poll();
                maxHeap.add(productAverages.get(i));
            }
        }

        return new ArrayList<>(maxHeap);
    }
}
