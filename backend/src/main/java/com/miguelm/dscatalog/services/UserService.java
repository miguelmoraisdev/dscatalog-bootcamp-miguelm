	package com.miguelm.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miguelm.dscatalog.dto.RoleDTO;
import com.miguelm.dscatalog.dto.UserDTO;
import com.miguelm.dscatalog.dto.UserInsertDTO;
import com.miguelm.dscatalog.dto.UserUpdateDTO;
import com.miguelm.dscatalog.entities.Role;
import com.miguelm.dscatalog.entities.User;
import com.miguelm.dscatalog.repositories.RoleRepository;
import com.miguelm.dscatalog.repositories.UserRepository;
import com.miguelm.dscatalog.services.exceptions.DatabaseException;
import com.miguelm.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(PageRequest pageRequest){
		Page<User> list = repository.findAll(pageRequest);
		Page<UserDTO> pageDTO = list.map(x -> new UserDTO(x));
		return pageDTO;
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDTOToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getById(id);
			copyDTOToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
	
	private void copyDTOToEntity(UserDTO dto, User entity) {
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		for (RoleDTO roleDTO : dto.getRolesDTO()) {
			Role role = roleRepository.getById(roleDTO.getId());
			entity.getRoles().add(role);
		}
	}

}
