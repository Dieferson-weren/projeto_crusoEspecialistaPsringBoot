package com.cursoSpring.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursoSpring.vendas.domain.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
