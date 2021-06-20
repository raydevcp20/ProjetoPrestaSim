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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.google.gson.Gson;
import org.springframework.http.MediaType;

import br.ifpe.web3.Acesso.Usuario;
import br.ifpe.web3.Acesso.UsuarioDAO;
import br.ifpe.web3.model.Profissional;
import br.ifpe.web3.model.ProfissionalDAO;
import br.ifpe.web3.model.Solicitacao;
import br.ifpe.web3.model.SolicitacaoDAO;

@Controller
public class SolicitController {
	@Autowired
	private UsuarioDAO userDAO;
	
	@Autowired
	private ProfissionalDAO profDAO;

	@Autowired
	private SolicitacaoDAO solicDAO;
	
    @Autowired
    private EmailController emailService;
    

    public void testarEmail(String destinatario, Solicitacao solicitacao, Profissional profissional) {
    	String assunto = "Tem serviço novo solicitado na PRESTASIM";
    	String corpo   = "Olá, "+ profissional.getNome() + "! Aqui é da PRESTASIM prestadora de serviços residenciais, tudo bom?"+
					     "\nEstamos felizes em avisar que o(a) " + solicitacao.getNomeSolicitante() +" requisitou seus serviços de "+
					     profissional.getTipoServico().getNome() + " por "+solicitacao.getDiasdeServ()+" dias e logo logo irá te chamar.\n\n"+
					     "O serviço irá custar R$ "+solicitacao.getValorTotal()+" reais para você, funcionário da PRESTASIM!!\n\n\n"
					     +"Descrição:"+ solicitacao.getDescricaoPedido();
        emailService.enviar( destinatario, assunto, corpo);
    }

    
	@ModelAttribute("lista_Solicitacao")
	public List<Solicitacao> getLista() {
		
		return this.solicDAO.findAllByFinalizado(false);
	}

	
	@PostMapping("/salvarSolicitacao")
	public String cadastrarSolicitacao(Solicitacao solicitacao, RedirectAttributes ra, Model model,  Integer login) {

		if (solicitacao.getNomeSolicitante().trim().isEmpty()) {
			ra.addFlashAttribute("classErrorSolic", "Proibido cadastramento nulo");
			return "redirect:/cadastroSolicitacao";
		} else if (this.solicDAO.existsByNomeSolicitante(solicitacao.getNomeSolicitante())
				&& this.solicDAO.existsByProfissional(solicitacao.getProfissional())
				&& this.solicDAO.existsByDescricaoPedido(solicitacao.getDescricaoPedido())) {
			ra.addFlashAttribute("classErrorSolic", "SolicitaÃ§Ã£o jÃ¡ cadastrada!");
			return "redirect:/cadastroSolicitacao";
		}
		
		
		this.solicDAO.save(solicitacao);
		Usuario usuario = this.userDAO.getOne(login);
		model.addAttribute("usuarioLogado", usuario);
		
		//envio de email
		testarEmail(solicitacao.getProfissional().getEmail(), solicitacao, solicitacao.getProfissional());
		return "index";
	}
	
	@RequestMapping(value = "/carregarPrecoServico", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String carregarPrecoServico(@RequestParam Integer idProfissional) {
		
	   Profissional profissional = this.profDAO.getOne(idProfissional);
	   return new Gson().toJson(profissional.getTipoServico());
	}
	
	@GetMapping("/cadastroSolicitacao")
	public String cadastroSolicitacao(Solicitacao solicitacao, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		model.addAttribute("solicita", solicitacao);
		model.addAttribute("lista_Profissional", this.profDAO.findAll(Sort.by("nome")));
		return "cadastroSolicit";
	}

	@GetMapping("/listaSolicitacao")
	public String listaSolicit(@RequestParam Integer user, Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		
		return "listaSolicit";
	}
	
	@GetMapping("/listaSolicConcluida")
	public String listaSolicitConcluidas() {
		return "listaSolicFinal";
	}

	@GetMapping("/adm/alterarSolicitacao")
	public String alterarSolicitacao(Integer id, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		Solicitacao solicitacao = this.solicDAO.getOne(id);
		model.addAttribute("lista_Profissional", this.profDAO.findAll(Sort.by("nome")));
		model.addAttribute("solicita", solicitacao);
		return "cadastroSolicit";
	}

	@GetMapping("/adm/excluirSolicitacao")
	public String ExcluirSolicitacao(Integer id, @RequestParam Integer user, Model model) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		this.solicDAO.deleteById(id);
		return "redirect:/listaSolicitacao?user="+ usuario.getCodigo();
	}
	
	// Alterações Byanca
	@GetMapping("/adm/finalizarSolicitacao")
	public String irParaPaginaFinalizar(Integer id, Model model, @RequestParam Integer user) {
		Usuario usuario = this.userDAO.getOne(user);
		model.addAttribute("usuarioLogado", usuario);
		
		Solicitacao solicitacao = this.solicDAO.getOne(id);
		solicitacao.setFinalizado(true);
		
		this.solicDAO.save(solicitacao);
		Profissional prof = solicitacao.getProfissional(); 
		model.addAttribute("profissional", prof);
		
		return "avaliarServico";
	}

	 
}
