package br.ifpe.web3.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoServicoDAO extends JpaRepository<TipoServico, Integer>{

	boolean existsByNome(String nome);
	
	List<TipoServico> findByNomeContaining(String nome);


}
