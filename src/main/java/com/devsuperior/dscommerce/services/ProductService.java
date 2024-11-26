package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = productRepository.findById(id);
        if(result.isPresent()) {
            Product product = result.get();
            return convertProductToDTO(product);
        } else {
            throw new NoSuchElementException("Objeto de ID " + id + " não encontrado.");
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductDTO> productDTOS = products.map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getImgUrl()));
        return productDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = convertDTOToProduct(productDTO);
        product = productRepository.save(product);
        return convertProductToDTO(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product product = productRepository.getReferenceById(id);

        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setImgUrl(productDTO.imgUrl());

        productRepository.save(product);

        return convertProductToDTO(product);
    }

    @Transactional(rollbackFor = {Exception.class, SQLException.class})
    public void delete(Long id) throws SQLException{
        productRepository.deleteById(id);
    }

    private ProductDTO convertProductToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl()
        );
    }

    private Product convertDTOToProduct(ProductDTO productDTO) {
        return new Product(
                null,
                productDTO.name(),
                productDTO.description(),
                productDTO.price(),
                productDTO.imgUrl()
        );
    }

}
