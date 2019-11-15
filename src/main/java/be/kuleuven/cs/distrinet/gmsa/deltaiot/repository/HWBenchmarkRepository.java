package be.kuleuven.cs.distrinet.gmsa.deltaiot.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;

public interface HWBenchmarkRepository extends JpaRepository<HWBenchmark, UUID> {

	public Collection<HWBenchmark> findAllByAdaptationModelId(int modelId);
}
