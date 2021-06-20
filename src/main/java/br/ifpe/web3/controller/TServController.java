package br.ifpe.web3.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ifpe.web3.Acesso.Usuario;
import br.ifpe.web3.Acesso.UsuarioDAO;
import br.ifpe.web3.model.TipoServico;
import br.ifpe.web3.model.TipoServicoDAO;

@Controller
public class TServController {
	
	@Autowired
	private UsuarioDAO userDAO;
	
	@Autowired
	private TipoServicoDAO tsDAO;
	

	@ModelAttribute("lista_Tiposervico")
	public List<TipoServico> getLista() {
		return this.tsDAO.findAll(Sort.by("id"));
	}

	// ir para a pagina de cadastro
	@GetMapping("/adm/cadastroTServico")
	public String cadastroServico(TipoServico TipoServ, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		model.addAttribute("tipoServico", TipoServ);
		return "cadastroServico";
	}

	@GetMapping("listaTServico")
	public String listaServico(@RequestParam Integer user,  Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		return "listaServico";
	}

	@PostMapping("/adm/cadastrarTipoServ")
	public String salvarTipoServ(TipoServico TipoServ, Model model, RedirectAttributes ra, Integer login) {
		
		if (TipoServ.getNome().trim().isEmpty()) {
			ra.addFlashAttribute("classErrorTServ", "Proibido cadastramento nulo");
			return "redirect:/adm/cadastroTServico";
		} else if (this.tsDAO.existsByNome(TipoServ.getNome())) {
			ra.addFlashAttribute("classErrorTServ", "Tipo de Serviço já cadastrado!");
			return "redirect:/adm/cadastroTServico";
		}
		Usuario usuario = this.userDAO.getOne(login);
		model.addAttribute("usuarioLogado", usuario);
		
		this.tsDAO.save(TipoServ);
		return "index";
	}
	
	@GetMapping("/adm/alterarTipoServico")
	public String alterarTipoServ(Integer id, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		TipoServico Ts = this.tsDAO.getOne(id);
		model.addAttribute("tipoServico", Ts);
		return "cadastroServico";
	}

	@GetMapping("/adm/excluirTipoServico")
	public String ExcluirTipoServico(Integer id, @RequestParam Integer user, Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		this.tsDAO.deleteById(id);
		return "redirect:/listaTServico?user="+ usuario.getCodigo();
	}

}
