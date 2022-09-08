package com.VendingMachines.root.api;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.services.ProductService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
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
        List<ProductDTO> products = new ArrayList<>();
        for(Product product:productService.getAll()){
            products.add(Utils.convertProductToDTO(product));
        }
        return products;
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
        return Utils.convertProductToDTO(productService.findByID(ID));
    }

    @GetMapping(path = "/getByUsername/")
    public ProductDTO getProductByName(@RequestParam String name) {
        return Utils.convertProductToDTO(productService.findByName(name));
    }
}
