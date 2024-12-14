package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.IllegalParamTypeException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = productRepository.findById(id);
        Product product = result.orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado."));
        return convertProductToDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String page, String size) {
        Integer pageInteger = pageIsInteger(page);
        Integer sizeInteger = sizeIsInteger(size);
        Pageable pageable = PageRequest.of(pageInteger, sizeInteger);
        Page<Product> products = productRepository.searchByName(name, pageable);
        return products.map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getImgUrl()));
    }

    private Integer pageIsInteger(String page){
        try {
            return Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new IllegalParamTypeException("Valor do parâmetro page precisa ser numérico.");
        }
    }

    private Integer sizeIsInteger(String size){
        try {
            return Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new IllegalParamTypeException("Valor do parâmetro size precisa ser numérico.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = convertDTOToProduct(productDTO);
        product = productRepository.save(product);
        return convertProductToDTO(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);

            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setImgUrl(productDTO.getImgUrl());

            productRepository.save(product);

            return convertProductToDTO(product);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
    }

    @Transactional(rollbackFor = {Exception.class, SQLException.class}, propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!productRepository.existsById(id)) throw new ResourceNotFoundException("Recurso não encontrado.");
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial.");
        }
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
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getImgUrl()
        );
    }

}
