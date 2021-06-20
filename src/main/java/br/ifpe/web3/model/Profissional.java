package br.ifpe.web3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Profissional implements Serializable{
	
	private static final long serialVersionUID = -7253957184733461963L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nome;
	private String email;
	private String telefone;
	@ManyToOne
	private TipoServico tipoServico;
	@Column
	private Double mediaNota;
	
	@ElementCollection(targetClass=Double.class)
	private List<Double> notas = new ArrayList<Double>();
	

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
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public Double getMediaNota() {
		return mediaNota;
	}

	public void setMediaNota(Double mediaNota) {
		
		notas.add(mediaNota);
		Double soma = 0.0;
		for(int i = 0; i<notas.size();i++) {
			soma+=notas.get(i);
		}
		mediaNota = soma/notas.size();
		
		this.mediaNota = mediaNota;
	}

	
	
	
}
