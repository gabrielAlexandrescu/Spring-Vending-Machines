package com.VendingMachines.root.services;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.repositories.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Service
public class ProductService {
    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public void add(ProductDTO productDTO) {
        boolean valid = false;
        Product product = productDTOToEntity(productDTO);
        productsRepository.save(product);
    }
    public ProductDTO findByID(UUID ID){
        return productToDTO(productsRepository.findById(ID).orElseThrow(()->new IllegalStateException("The user with this ID doesn't exist!")));
    }
    public ProductDTO findByName(String name){
        List<Product> products = productsRepository.findAll();
        for(Product product:products){
            if(Objects.equals(product.getName(), name))
            {
                return productToDTO(product);
            }
        }
        return null;
    }
    private Product productDTOToEntity(ProductDTO productDTO){
        return new Product(productDTO.getName(),productDTO.getProductType());
    }
    private ProductDTO productToDTO(Product product){
        return new ProductDTO(product.getID(),product.getName(),product.getProductType());

    }
    public void update(UUID ID,ProductDTO updatedProduct){
        Product updated = productDTOToEntity(updatedProduct);
        Product oldProduct = productDTOToEntity(findByID(ID));
        oldProduct.setProductType(updated.getProductType());
        oldProduct.setName(updated.getName());
        productsRepository.save(oldProduct);
    }
    public void delete(UUID ID){
        Product toBeDeleted = productDTOToEntity(findByID(ID));
        productsRepository.delete(toBeDeleted);
    }
    public List<ProductDTO> getAll(){
        List<Product> products = productsRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(Product product: products){
            productDTOS.add(productToDTO(product));
        }
        return productDTOS;
    }
}
