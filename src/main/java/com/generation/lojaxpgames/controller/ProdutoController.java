package com.generation.lojaxpgames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojaxpgames.model.Produto;
import com.generation.lojaxpgames.repository.CategoriaRepository;
import com.generation.lojaxpgames.repository.ProdutoRepository;

import jakarta.validation.Valid;



@RestController // Indica que ProdutoController é a classe que vai receber as requisições.
@RequestMapping("/produtos") // Usado para mapear solicitações da classe ProdutoController, ou seja define a URL padrão http://localhost:8080/produtos.
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired // É a injeção de dependência para aplicar a inversão de controle, ou seja defise quais classes serao instanciadas.			
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll()); // findAll método padrão da Jpa, que retornará todos os objetos da Classe Produto persistidos no Banco de dados.														
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId())) // Método existsById(Long id), da Interface CategoriaRepository (Herança da Interface JpaRepository) checaremos se o id do Objeto categoria, da Classe Categoria, inserido no Objeto produto, da Classe Produto, existe.
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		 if (produtoRepository.existsById(produto.getId())) {

	            if (categoriaRepository.existsById(produto.getCategoria().getId()))
	                return ResponseEntity.status(HttpStatus.OK)
	                        .body(produtoRepository.save(produto));

	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
	}
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
		 

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		produtoRepository.deleteById(id);
	}
}
