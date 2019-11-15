package be.kuleuven.cs.distrinet.gmsa.deltaiot.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.formdata.RegistrationFormData;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.sessions.SessionManager;

@Controller
@RequestMapping(RegistrationController.PATH)
public class RegistrationController {
	
	public static final String PATH = "/register";

	@Autowired
	private AccountService accounts;

	@GetMapping
	public String showRegistrationForm(Model model) {
		if (!model.containsAttribute("registrationInfo")) {
			model.addAttribute("registrationInfo", new RegistrationFormData());
		}
		return "register-account";
	}

	@PostMapping
	public String createAccount(Model model,
			@ModelAttribute("registrationInfo") RegistrationFormData formData,
			BindingResult result,
			final RedirectAttributes redirectAttrs,
			HttpServletResponse response) {
		// check if passwords match
		if (!formData.getPassword().contentEquals(formData.getConfirmPassword())) {
			model.addAttribute("error", "Passwords do not match");
			return "register-account";
		}
		// create new account
		Account newAccountInfo = new Account(formData.getUsername(), formData.getEmail(), formData.getPassword());
		try {
			var savedAccount = accounts.createNewAccount(newAccountInfo);
			redirectAttrs.addFlashAttribute("info", "Account created successfully!");
			SessionManager.startSession(savedAccount.getUsername(), response);
			return "redirect:" + ProfileController.PATH + "/" + savedAccount.getUsername();
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("error", "Could not create account: " + e.getMessage());
			return "register-account";
		}
	}
}
