package be.kuleuven.cs.distrinet.gmsa.deltaiot.service.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.infrastructure.BenchmarkRunner;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.repository.HWBenchmarkRepository;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AdaptationModelService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.HardwareBenchmarkService;

@Service
public class HardwareBenchmarkServiceImpl implements HardwareBenchmarkService {

	@Autowired
	private HWBenchmarkRepository benchmarks;

	@Autowired
	private AdaptationModelService models;

	@Override
	public Collection<HWBenchmark> getHWBenchmarksForModel(int modelID) {
		return benchmarks.findAllByAdaptationModelId(modelID);
	}

	@Override
	public List<HWBenchmark> getHWBenchmarksForUser(String username) {
		var uploadedModels = models.getUploadedModelsFor(username);
		return uploadedModels.stream().flatMap(model -> benchmarks.findAllByAdaptationModelId(model.getId()).stream())
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HWBenchmark> getHWBenchmarkById(UUID id) {
		return benchmarks.findById(id);
	}

	@Override
	public List<HWBenchmark> getAllHWBenchmarks() {
		return benchmarks.findAll();
	}

	@Override
	public HWBenchmark requestNewBenchmark(int modelId) {
		var modelMaybe = models.getModelById(modelId);
		if (modelMaybe.isEmpty()) {
			throw new NotFoundException("No model with id " + modelId);
		}
		var model = modelMaybe.get();
		var benchmark = new HWBenchmark();
		benchmark.setAdaptationModelId(modelId);
		final var savedBenchmark = benchmarks.save(benchmark);
		BenchmarkRunner.INSTANCE.scheduleBenchmark(model, result -> {
			savedBenchmark.setStartedAt(Instant.now());
			savedBenchmark.setResultAndSuccess(result.getResult(), result.isSuccess());
			benchmarks.save(savedBenchmark);
		});
		return savedBenchmark;
	}
}
