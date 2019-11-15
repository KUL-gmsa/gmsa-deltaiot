package be.kuleuven.cs.distrinet.gmsa.deltaiot.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.repository.ModelRepository;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AdaptationModelService;

@Service
public class AdaptationModelServiceImpl implements AdaptationModelService {

	@Autowired
	private ModelRepository models;
	
	@Override
	public Optional<AdaptationModel> getModelById(int id) {
		return models.findById(id);
	}
	
	@Override
	public Collection<AdaptationModel> getAllUploadedModels() {
		return models.findAll();
	}
	
	@Override
	public Collection<AdaptationModel> getUploadedModelsFor(String username) {
		return models.findAllByOwner(username);
	}
	
	@Override
	public AdaptationModel createNewModel(String username, AdaptationModel info) {
		info.setId(-1);
		info.setOwner(username);
		return models.save(info);
	}
	
	@Override
	public AdaptationModel updateModel(AdaptationModel newModel) {
		return models.save(newModel);
	}
	
	@Override
	public void deleteModel(int id) {
		models.deleteById(id);
	}

}
