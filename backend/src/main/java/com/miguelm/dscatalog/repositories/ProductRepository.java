package com.miguelm.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelm.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
