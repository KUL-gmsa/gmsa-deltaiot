package be.kuleuven.cs.distrinet.gmsa.deltaiot.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AuthenticationService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.formdata.LoginFormData;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.sessions.SessionManager;

@Controller
public class LoginController {

	public static final String LOGIN_PATH = "/login";
	public static final String LOGOUT_PATH = "/logout";

	@Autowired
	private AuthenticationService authnService;
		
	@Autowired
	private AccountService accountService;
	
	@GetMapping(LOGIN_PATH)
	public String startLogin() {
		return "redirect:" + IndexController.PATH;
	}
	
	@PostMapping(LOGIN_PATH)
	public String login(Model model, 
			@ModelAttribute("loginInfo") LoginFormData info,
			HttpServletResponse response,
			RedirectAttributes redirectAttr) {
		var username = info.getUsername();
		var password = info.getPassword();
		if (authnService.authenticate(username, password) != null) {
			accountService.recordLogin(username);
			SessionManager.startSession(username, response);
			return "redirect:" + ProfileController.PATH + "/" + username;			
		} else {
			redirectAttr.addFlashAttribute("error", "Incorrect username or password, please try again.");
			return "redirect:" + IndexController.PATH;
		}
	}
		
	@GetMapping(LOGOUT_PATH)
	public String logout(HttpServletResponse response) {
		SessionManager.endSession(response);
		return "redirect:" + IndexController.PATH;
	}
}
