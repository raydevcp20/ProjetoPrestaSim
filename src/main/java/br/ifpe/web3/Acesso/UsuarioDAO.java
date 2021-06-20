package br.ifpe.web3.Acesso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDAO extends JpaRepository<Usuario, Integer>{

	public Usuario findByLoginAndSenha(String login, String senha);
}
