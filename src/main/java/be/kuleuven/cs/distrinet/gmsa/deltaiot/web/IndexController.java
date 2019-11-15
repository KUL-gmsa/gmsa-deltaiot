package be.kuleuven.cs.distrinet.gmsa.deltaiot.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.formdata.LoginFormData;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.sessions.SessionManager;

@Controller
@RequestMapping(IndexController.PATH)
public class IndexController {
	
	public static final String PATH = "/";

	
	@GetMapping
	public String index(Model model, HttpServletRequest request) {
		if (SessionManager.belongsToActiveSession(request)) {
			return "redirect:" + ProfileController.PATH + "/" + SessionManager.getAssociatedUsername(request);
		}
		model.addAttribute("loginInfo", new LoginFormData());
		return "index";
	}
}
