package com.cursoSpring.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursoSpring.vendas.domain.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
