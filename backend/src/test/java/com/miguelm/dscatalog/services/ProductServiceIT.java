package com.miguelm.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.miguelm.dscatalog.dto.ProductDTO;
import com.miguelm.dscatalog.repositories.ProductRepository;
import com.miguelm.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long noExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		noExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenExistsId() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}

	@Test
	public void deleteShouldResourceNotFoundExceptionWhenDoesNotExistId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(noExistingId);
		} );
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenPage0Size10() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
		
	}
	
	@Test
	public void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenPageSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
		
	}
	
	
}
