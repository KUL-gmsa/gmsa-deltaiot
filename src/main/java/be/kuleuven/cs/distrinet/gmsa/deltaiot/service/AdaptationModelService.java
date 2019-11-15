package be.kuleuven.cs.distrinet.gmsa.deltaiot.service;

import java.util.Collection;
import java.util.Optional;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;

public interface AdaptationModelService {

	Collection<AdaptationModel> getUploadedModelsFor(String username);

	Optional<AdaptationModel> getModelById(int id);

	AdaptationModel createNewModel(String username, AdaptationModel info);

	Collection<AdaptationModel> getAllUploadedModels();

	AdaptationModel updateModel(AdaptationModel model);

	void deleteModel(int id);

}
