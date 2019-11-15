package be.kuleuven.cs.distrinet.gmsa.deltaiot.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;

public interface HardwareBenchmarkService {
	
	Collection<HWBenchmark> getHWBenchmarksForModel(int modelID);

	List<HWBenchmark> getAllHWBenchmarks();
	
	List<HWBenchmark> getHWBenchmarksForUser(String username);

	Optional<HWBenchmark> getHWBenchmarkById(UUID id);

	HWBenchmark requestNewBenchmark(int modelId);
		
}
