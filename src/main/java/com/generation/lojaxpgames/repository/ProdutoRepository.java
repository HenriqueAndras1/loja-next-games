package com.generation.lojaxpgames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


import com.generation.lojaxpgames.model.Produto;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {// Importamos a extends Interface JPA, e Model Produto.

	public List <Produto> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);
}
