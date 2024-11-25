package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> result = productRepository.findById(id);
        Product product = result.get();
        ProductDTO productDTO = new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl());
        return productDTO;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductDTO> productDTOS = products.map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getImgUrl()));
        return productDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO insert(ProductDTO productDTO){
        Product product = convertDTOToProduct(productDTO);
        product = productRepository.save(product);
        return convertProductToDTO(product);
    }

    private ProductDTO convertProductToDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl()
        );
    }

    private Product convertDTOToProduct(ProductDTO productDTO){
        return new Product(
                null,
                productDTO.name(),
                productDTO.description(),
                productDTO.price(),
                productDTO.imgUrl()
        );
    }

}
