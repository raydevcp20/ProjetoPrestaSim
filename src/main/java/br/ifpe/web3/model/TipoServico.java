package br.ifpe.web3.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoServico implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1507436269187003288L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nome;
	private int valorDiaria;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(int valorDiaria) {
		this.valorDiaria = valorDiaria;
	}
	

	
}
