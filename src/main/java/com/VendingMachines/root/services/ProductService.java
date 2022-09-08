package com.VendingMachines.root.services;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Product product = productDTOToEntity(productDTO);
        productsRepository.save(product);
    }
    public Product findByID(UUID ID){
        return productsRepository.findById(ID).orElseThrow(()->new IllegalStateException("The product with this ID doesn't exist!"));
    }
    public Product findByName(String name){
        List<Product> products = productsRepository.findAll();
        for(Product product:products){
            if(Objects.equals(product.getName(), name))
            {
                return product;
            }
        }
        return null;
    }
    private Product productDTOToEntity(ProductDTO productDTO){
        return new Product(productDTO.getName(),productDTO.getProductType());
    }
    private ProductDTO productToDTO(Product product){
        ProductDTO productDTO = new ProductDTO(product.getName(),product.getProductType());
        productDTO.setID(product.getID());
        return productDTO;
    }
    public void update(UUID ID,ProductDTO updatedProduct){
        Product updated = productDTOToEntity(updatedProduct);
        Product oldProduct = findByID(ID);
        oldProduct.setProductType(updated.getProductType());
        oldProduct.setName(updated.getName());
        productsRepository.save(oldProduct);
    }
    public void delete(UUID ID){
        Product toBeDeleted = findByID(ID);
        productsRepository.delete(toBeDeleted);
    }
    public List<Product> getAll(){
        return productsRepository.findAll();
    }
}
