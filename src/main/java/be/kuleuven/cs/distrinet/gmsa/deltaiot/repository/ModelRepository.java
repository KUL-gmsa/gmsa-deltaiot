package be.kuleuven.cs.distrinet.gmsa.deltaiot.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;

public interface ModelRepository extends JpaRepository<AdaptationModel, Integer> {

	public Collection<AdaptationModel> findAllByOwner(String username);
}
