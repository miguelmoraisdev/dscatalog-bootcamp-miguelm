package com.miguelm.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.miguelm.dscatalog.dto.ProductDTO;
import com.miguelm.dscatalog.entities.Category;
import com.miguelm.dscatalog.entities.Product;
import com.miguelm.dscatalog.repositories.CategoryRepository;
import com.miguelm.dscatalog.repositories.ProductRepository;
import com.miguelm.dscatalog.services.exceptions.DatabaseException;
import com.miguelm.dscatalog.services.exceptions.ResourceNotFoundException;
import com.miguelm.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existingId;
	private Long noExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		noExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		dto = Factory.createProductDTO();
		product = Factory.createProduct();
		
		Mockito.when(categoryRepository.getById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getById(noExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.findAll((PageRequest)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.getById(existingId)).thenReturn(product);
		Mockito.when(repository.getById(noExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageRequest);
		
	}
	
	@Test
	public void findByIdShouldReturnEntityWhenIdExists() {
		
		 ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(noExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(noExistingId);
	}
	
	
	@Test
	public void updateShouldReturnsProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(noExistingId, dto);
		});
		
		
		
	}
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(noExistingId);
		} );
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(noExistingId);
		
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		} );
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
		
	}
	
	

}
