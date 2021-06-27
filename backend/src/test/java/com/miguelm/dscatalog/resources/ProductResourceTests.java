package com.miguelm.dscatalog.resources;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelm.dscatalog.dto.ProductDTO;
import com.miguelm.dscatalog.services.ProductService;
import com.miguelm.dscatalog.services.exceptions.DatabaseException;
import com.miguelm.dscatalog.services.exceptions.ResourceNotFoundException;
import com.miguelm.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	private Long existingId;
	private Long noExistingId;
	private Long dependentId;
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		noExistingId = 2L;
		dependentId = 3L;
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(noExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(noExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(noExistingId);
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void findAllShouldReturnsPage() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
				
		result.andExpect(status().isOk());
		
	}
	
	@Test
	public void findByIdShouldReturnsProductDTOWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{Id}", existingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	
	@Test
	public void findByIdShouldNotFoundWhenDoesNotIdExist() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products/{Id}", noExistingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void updateShouldNotFoundWhenDoesNotIdExist() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}", noExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{Id}", existingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenDoesNotExistsId() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{Id}", noExistingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
	}
	
	
	
	
	
}	
