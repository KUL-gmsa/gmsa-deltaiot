package be.kuleuven.cs.distrinet.gmsa.deltaiot.web;

import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AdaptationModelService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.HardwareBenchmarkService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.web.sessions.SessionManager;

@Controller
@RequestMapping(ProfileController.PATH)
public class ProfileController {

	public static final String PATH = "/profile";

	@Autowired
	private AccountService accounts;

	@Autowired
	private AdaptationModelService models;

	@Autowired
	private HardwareBenchmarkService benchmarks;

	@GetMapping("{username}")
	public String showProfile(Model model, HttpServletRequest request, @PathVariable("username") String viewedProfile) {
		if (SessionManager.belongsToActiveSession(request) && viewedProfile != null) {
			model.addAttribute("currentUser", SessionManager.getAssociatedUsername(request));
			var account = accounts.findAccountById(viewedProfile);
			if (account != null) {
				if (account.isAdmin()) {
					return adminProfile(account, model);
				} else {
					return userProfile(account, model);
				}
			} else {
				throw new NotFoundException("No such profile");
			}
		} else {
			throw new NotFoundException("No such profile");
		}
	}

	private String userProfile(Account account, Model model) {
		model.addAttribute("account", account);
		model.addAttribute("models", models.getUploadedModelsFor(account.getUsername()));
		var bms = benchmarks.getHWBenchmarksForUser(account.getUsername());
		Collections.sort(bms,
				Comparator.comparing(HWBenchmark::getStartedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
		model.addAttribute("benchmarks", bms);
		return "profile";
	}

	private String adminProfile(Account account, Model model) {
		model.addAttribute("account", account);
		model.addAttribute("pendingAccounts", accounts.getAccountsPendingApproval());
		var bms = benchmarks.getAllHWBenchmarks();
		Collections.sort(bms,
				Comparator.comparing(HWBenchmark::getStartedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
		model.addAttribute("benchmarks", bms);
		model.addAttribute("benchmarks", bms);
		return "admin-profile";
	}
}
