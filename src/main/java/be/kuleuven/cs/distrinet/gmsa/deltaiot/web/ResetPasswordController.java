package be.kuleuven.cs.distrinet.gmsa.deltaiot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AuthenticationService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.formdata.ResetPasswordFormData;

@Controller
public class ResetPasswordController {

	@Autowired
	private AuthenticationService authnService;
	
	@GetMapping("/resetPassword")
	public String resetPassword(Model model) {
		model.addAttribute("passwordInfo", new ResetPasswordFormData());
		return "reset-password";
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(Model model, @ModelAttribute("passwordInfo") ResetPasswordFormData info, RedirectAttributes attr) {
		String username = info.getUsername();
		var account = authnService.resetPassword(username);
		if (account != null) {
			attr.addFlashAttribute("info", "A mail has been sent to " + account.getEmail() + " with instructions on how to reset your password.");
		} else {
			attr.addFlashAttribute("error", "Account " + username + " not found.");
		}
		return "redirect:" + IndexController.PATH;
	}

}
