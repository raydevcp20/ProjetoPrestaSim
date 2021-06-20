package br.ifpe.web3.model;

import java.util.List;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalDAO extends JpaRepository<Profissional, Integer>{

	boolean existsByNome(String nome);

	boolean existsByTipoServico(String nome);
	
	List<Profissional> findByNomeContaining(String nome);

	List<Profissional> findAllByOrderByMediaNotaDesc();

}
