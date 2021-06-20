package br.ifpe.web3.Acesso;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ifpe.web3.model.Solicitacao;


@Controller
public class AcessoController {

	@Autowired
	private UsuarioDAO UsuarioDAO;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}
	@GetMapping("/cadastrarUser")
	public String cadastrarUsuario (Usuario usuario) {
		return "cadastroUser" ;
	}
	@GetMapping("/adm/alterarUsuario")
	public String alterarUsuario (Integer codigo, Model model) {
		Usuario user = this.UsuarioDAO.getOne(codigo);
		model.addAttribute("usuario", user);
		return "cadastroUser";
	}
	@GetMapping("/adm/home")
	public String home() {
		return "index";
	}
	@PostMapping("/salvarUsuario")
	public String salvarUsuario (Usuario user, RedirectAttributes ra) {
		this.UsuarioDAO.save(user);
		ra.addFlashAttribute("SucessoAcesso", "Dados salvos com sucesso!");
		return "redirect:/";
	}
	@PostMapping("/login")
	public String efetuarLogin(String login, String senha, HttpSession session, RedirectAttributes ra, Model model) {
		
		senha = Encriptografar.md5(senha);
		Usuario usuarioLogado = this.UsuarioDAO.findByLoginAndSenha(login, senha);
		
		if (usuarioLogado == null) {
			ra.addFlashAttribute("ErrorAcesso", "Login ou senha inválidos!");
			return "redirect:/";
		} else {
			session.setAttribute("usuarioLogado", usuarioLogado);
			model.addAttribute("usuarioLogado", usuarioLogado);
			return "index";
		}
	}
	
	@GetMapping("/adm/alterarUser")
	public String alterarSolicitacao(Integer id, Model model) {
		Usuario usuario = this.UsuarioDAO.getOne(id);
		model.addAttribute("usuario", usuario);
		//ra.addFlashAttribute("SucessoAcesso", "Dados alterados com sucesso!");
		return "cadastroUser";
	}

	@GetMapping("/adm/DesativarUser")
	public String ExcluirSolicitacao(Integer id, RedirectAttributes ra) {
		
		this.UsuarioDAO.deleteById(id);
		ra.addFlashAttribute("SucessoAcesso", "Conta excluida com sucesso!");
		return "redirect:/";
	}
	
	@GetMapping("/sair")
	public String sair(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
	@GetMapping("/acessoNegado")
	public String acessoNegado() {
		return "AcessoNegado";
	}
}
