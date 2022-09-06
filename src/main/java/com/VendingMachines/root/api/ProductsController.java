package com.VendingMachines.root.api;

import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.services.ProductService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.UUID;
@EnableSwagger2
@RestController
@RequestMapping(path = "/api/products/")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getAll();
    }

    @PostMapping
    public void addProduct(@RequestParam String name, @RequestParam ProductType productType) {
        ProductDTO productDTO = new ProductDTO(name,productType);
        productService.add(productDTO);
    }

    @PutMapping
    public void updateProduct(@RequestParam UUID ID, @RequestBody ProductDTO productDTO) {
        productService.update(ID,productDTO);
    }

    @DeleteMapping
    public void deleteProduct(@RequestParam UUID ID) {
        productService.delete(ID);
    }

    @GetMapping(path = "/getByID/")
    public ProductDTO getProductByID(UUID ID) {
        return productService.findByID(ID);
    }

    @GetMapping(path = "/getByUsername/")
    public ProductDTO getProductByName(@RequestParam String name) {
        return productService.findByName(name);
    }
}
