package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;


@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        try {
            ProductDTO productDTO = productService.findById(id);
            return ResponseEntity.ok(productDTO);
        } catch(NoSuchElementException | NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
}

@GetMapping
public ResponseEntity<Page<ProductDTO>> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
    Page<ProductDTO> productDTOS = productService.findAll(pageable);
    return ResponseEntity.ok(productDTOS);
}

@PostMapping
public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
    productDTO = productService.insert(productDTO);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(productDTO.id())
            .toUri();

    return ResponseEntity.created(location).body(productDTO);
}

@PutMapping("/{id}")
public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductDTO productDTO) {
    productService.update(id, productDTO);
    return ResponseEntity.noContent().build();
}

}