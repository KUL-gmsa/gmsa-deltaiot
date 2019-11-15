package be.kuleuven.cs.distrinet.gmsa.deltaiot.api.v1;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AdaptationModelService;

/**
 * REST API for the adaptation models
 * @author koeny
 *
 */
@RestController
@RequestMapping(AdaptationModelRESTController.PATH)
public class AdaptationModelRESTController extends AbstractBaseRESTController {
	
	public static final String PATH = "/api/v1/adaptationmodel";
	
	
	@Autowired
	private AdaptationModelService modelService;
	

	@GetMapping
	public Collection<AdaptationModel> getAllModelsForCurrentUser(@RequestHeader(TOKEN_HEADER_NAME) String token) {
		var account = validateApiToken(token);
		if (account.isAdmin()) {
			return modelService.getAllUploadedModels();
		} else {
			return modelService.getUploadedModelsFor(account.getUsername());
		}
	}
	
	@PostMapping
	public AdaptationModel uploadNewModel(@RequestHeader(TOKEN_HEADER_NAME) String token, @RequestBody AdaptationModel model) {
		var account = validateApiToken(token);
		return modelService.createNewModel(account.getUsername(), model);
	}
	
	@GetMapping("{id}")
	public AdaptationModel getModel(@RequestHeader(TOKEN_HEADER_NAME) String token, @PathVariable int id) {
		validateApiToken(token);
		Optional<AdaptationModel> model = modelService.getModelById(id);
		if (model.isPresent()) {
			return model.get();
		}
		throw new NotFoundException("No adaptation model with id " + id);
	}
	
	@PutMapping("{id}")
	public AdaptationModel updateModel(@RequestHeader(TOKEN_HEADER_NAME) String token, @PathVariable int pathId, @RequestBody AdaptationModel model) {
		validateApiToken(token);
		model.setId(pathId);
		return modelService.updateModel(model);
	}
	
	@DeleteMapping("{id}")
	public void deleteModel(@RequestHeader(TOKEN_HEADER_NAME) String token, @PathVariable int id) {
		validateApiToken(token);
		modelService.deleteModel(id);
	}
    
}