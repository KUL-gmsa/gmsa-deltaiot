package be.kuleuven.cs.distrinet.gmsa.deltaiot.api.v1;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.HardwareBenchmarkService;

/**
 * REST API for the benchmarks on the hardware infrastructure
 * @author koeny
 *
 */
@RestController
@RequestMapping(BenchmarkRESTController.PATH)
public class BenchmarkRESTController extends AbstractBaseRESTController {
	
	public static final String PATH = "/api/v1/benchmark";
	
	
	@Autowired
	private HardwareBenchmarkService benchmarkService;
	

	@GetMapping
	public Collection<HWBenchmark> getAllBenchmarksForCurrentUser(@RequestHeader(TOKEN_HEADER_NAME) String token) {
		var account = validateApiToken(token);
		if (account.isAdmin()) {
			return benchmarkService.getAllHWBenchmarks();
		} else {
			return benchmarkService.getHWBenchmarksForUser(account.getUsername());
		}
	}
		
	@GetMapping("{id}")
	public HWBenchmark getBenchmark(@RequestHeader(TOKEN_HEADER_NAME) String token, @PathVariable UUID id) {
		validateApiToken(token);
		Optional<HWBenchmark> benchmark = benchmarkService.getHWBenchmarkById(id);
		if (benchmark.isPresent()) {
			return benchmark.get();
		}
		throw new NotFoundException("No benchmark with id " + id);
	}
	
	@PostMapping("new/{modelId}")
	public HWBenchmark requestNewBenchmark(@RequestHeader(TOKEN_HEADER_NAME) String token, @PathVariable int modelId) {
		validateApiToken(token);
		HWBenchmark benchmark = benchmarkService.requestNewBenchmark(modelId);
		return benchmark;
	}

    
}