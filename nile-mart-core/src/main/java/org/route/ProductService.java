package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;


    public ProductService(@Qualifier("inMemoryProductRepository") ProductRepository repository,
                          PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    public BigDecimal getPrice(String sku, int qty) {
        Product product = repository.findBySku(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new RuntimeException("Product isn't available."));
        return pricingService.getPrice(product, qty);
    }


    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findBySku(String sku) {
        return repository.findBySku(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new StoreException("Product not found or unavailable: " + sku));
    }


    public void reduceStock(String sku, int qty) {
        if (qty <= 0) throw new StoreException("Quantity must be greater than zero.");
        Product product = findBySku(sku);
        if (product.getStockQuantity() < qty) {
            throw new StoreException("Insufficient stock for " + product.getName()
                    + ". Available: " + product.getStockQuantity());
        }
        product.setStockQuantity(product.getStockQuantity() - qty);
        repository.save(product);
    }

    public void checkStock(String sku, int qty) {
        if (qty <= 0) throw new StoreException("Quantity must be greater than zero.");
        Product product = findBySku(sku);
        if (product.getStockQuantity() < qty) {
            throw new StoreException("Insufficient stock for " + product.getName()
                    + ". Available: " + product.getStockQuantity());
        }
    }
}
