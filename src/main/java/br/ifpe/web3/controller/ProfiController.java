package br.ifpe.web3.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import br.ifpe.web3.model.Profissional;
import br.ifpe.web3.model.ProfissionalDAO;
import br.ifpe.web3.model.TipoServicoDAO;

@Controller
public class ProfiController {
	@Autowired
	private UsuarioDAO userDAO;
	
	@Autowired
	private ProfissionalDAO profDAO;

	@Autowired
	private TipoServicoDAO TServDAO;
	

	@GetMapping("/adm/cadastroProfissional")
	public String cadastroFuncio(Profissional profissional, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		model.addAttribute("profi", profissional);
		model.addAttribute("lista_Tiposervico", this.TServDAO.findAll(Sort.by("nome")));
		return "cadastroFuncio";
	}

	@GetMapping("/listaProfissional")
	public String listaFuncio(@RequestParam Integer user, Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		return "listaFuncio";
	}

	@PostMapping("/adm/salvarProfi")
	public String cadastrarProfissional(Profissional profissional, RedirectAttributes ra,  Model model, Integer login) {

		if (profissional.getNome().trim().isEmpty()) {
			ra.addFlashAttribute("classErrorFunc", "Proibido cadastramento nulo");
			return "redirect:/adm/cadastroProfissional";
		} else if (this.profDAO.existsByNome(profissional.getNome())
				&& this.profDAO.existsByTipoServico(profissional.getTipoServico().getNome())) {
			ra.addFlashAttribute("classErrorFunc", "Profissional já cadastrado!");
			return "redirect:/adm/cadastroProfissional";
		}
		this.profDAO.save(profissional);
		
		Usuario usuario = this.userDAO.getOne(login);
		model.addAttribute("usuarioLogado", usuario);
		
		return "index";
	}

	@ModelAttribute("lista_Profissional")
	public List<Profissional> getLista() {
		return this.profDAO.findAll(Sort.by("id"));
	}

	@GetMapping("/adm/alterarProfissional")
	public String alterarProfissional(Integer id, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		Profissional professional = this.profDAO.getOne(id);
		model.addAttribute("lista_Tiposervico", this.TServDAO.findAll(Sort.by("nome")));
		model.addAttribute("profi", professional);
		return "cadastroFuncio";
	}

	@GetMapping("/adm/excluirProfissional")
	public String ExcluirProfissional(Integer id, @RequestParam Integer user, Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		this.profDAO.deleteById(id);
		return "redirect:/listaProfissional?user="+ usuario.getCodigo();
	}

	// avaliacao
	@PostMapping("/adm/salvarAvaliacao")
	public String SalvarAvaliacao(Profissional profissional, RedirectAttributes ra, Model model, Integer login) {
		
		Profissional profissional2 = this.profDAO.getOne(profissional.getId());
		profissional2.setMediaNota(profissional.getMediaNota());
		
		this.profDAO.save(profissional2);
		
		Usuario usuario = this.userDAO.getOne(login);
		model.addAttribute("usuarioLogado", usuario);
		return "index";
	}
	
	@ModelAttribute("lista_ProfissionalR")
	public List<Profissional> getListaRanking() {
		return this.profDAO.findAllByOrderByMediaNotaDesc();
	}
	
	@GetMapping("/rankingProfi")
	public String listaRanking() {
		return "rankingProf";
	}

}
