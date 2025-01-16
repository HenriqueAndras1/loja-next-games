package com.generation.lojaxpgames.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY, informamos ao provedor que será atributo de PK e serão gerados pela coluna de AI no banco de dados.
	private Long id;
	
	@NotBlank(message = "O atributo Nome é Obrigatório! ")
	@Size(min = 2, max = 30, message = "O atributo nome deve conter no mínimo 2 e no máximo 30 caracteres!")
	private String nome; //Inserir nome do jogo
	
	@NotBlank(message = "O atributo descrição é Obrigatório! ")
	@Size(min = 15, max = 255, message = "O atributo descrição deve conter no mínimo 15 e no máximo 255 caracteres!")
	private String descricao; // Descrição do jogo
	
	@NotBlank(message = "O atributo Preço é Obrigatório! ")
	@Size(min = 4, max = 9, message = "O atributo Preço deve conter no mínimo 4 e no máximo 9 caracteres!")
	private String preco; // Preenchimento do preço
	
	@NotBlank(message = "O atributo Data de Lançamento é Obrigatório! ")
	@Size(min = 10, max = 10, message = "O atributo Data de lançamento deve conter no mínimo 10 e no máximo 10 caracteres!")
	private String datalancamento; // Data de lançamento do jogo
	
	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Categoria categoria;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getDatalancamento() {
		return datalancamento;
	}

	public void setDatalancamento(String datalancamento) {
		this.datalancamento = datalancamento;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
}
