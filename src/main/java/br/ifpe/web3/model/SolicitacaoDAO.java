package br.ifpe.web3.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolicitacaoDAO extends JpaRepository<Solicitacao, Integer>{

	boolean existsByNomeSolicitante(String nomeSolicitante);

	boolean existsByData(LocalDate data);

	boolean existsByProfissional(Profissional profissional);

	boolean existsByDescricaoPedido(String descricaoPedido);

	List<Solicitacao> findAllByFinalizado(Boolean i);

	
}
