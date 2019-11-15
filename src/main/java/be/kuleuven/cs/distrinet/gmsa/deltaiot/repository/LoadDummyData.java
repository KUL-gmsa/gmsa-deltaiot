package be.kuleuven.cs.distrinet.gmsa.deltaiot.repository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.infrastructure.BenchmarkRunner;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.HWBenchmark;

@Configuration
class LoadDummyData {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoadDummyData.class);

	@Bean
	CommandLineRunner initAccountDatabase(AccountRepository repository) {
		return args -> {
			Account user = Account.create("admin", "admin@example.com", "admin", true);
			user.setHardwareAuthorized(true);
			user.setHardwareAuthorizationDecision(Instant.now().minus(Duration.ofDays(7)));
			log.info("Preloading " + repository.save(user));
			
			user = Account.create("john", "john@example.com", "john", false);
			user.setHardwareAuthorized(true);
			user.setHardwareAuthorizationDecision(Instant.now().minus(Duration.ofDays(3)));
			log.info("Preloading " + repository.save(user));
			
			user = Account.create("mary", "mary@example.com", "mary", false);
			log.info("Preloading " + repository.save(user));
		};
	}
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Bean
	CommandLineRunner initModelDatabase(ModelRepository repository, HWBenchmarkRepository benchmarks) {
		return args -> {
			AdaptationModel model = new AdaptationModel();
			model.setOwner("john");
			model.setName("John's first adaptation model");
			model.setClassname("john.FirstStrategy");
			model.setFilename("john/FirstStrategy.java");
			model.setCode(Files.readString(Paths.get(resourceLoader.getResource("classpath:" + model.getFilename()).getURI())));
			model = repository.save(model);
			log.info("Preloading " + model);
						
			HWBenchmark sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess(BenchmarkRunner.INSTANCE.runBenchmarkNow(model).getResult(), true);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(10)));
			benchmarks.save(sim);
			
			sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess("Unknown error", false);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(5)));
			benchmarks.save(sim);
			log.info("Preloading " + sim);
			
			var hwbench = new HWBenchmark();
			hwbench.setAdaptationModelId(model.getId());
			hwbench.setResultAndSuccess(BenchmarkRunner.INSTANCE.runBenchmarkNow(model).getResult(), true);
			hwbench.setStartedAt(Instant.now().minus(Duration.ofHours(1)));
			benchmarks.save(hwbench);
			log.info("Preloading " + hwbench);
			
			model = new AdaptationModel();
			model.setOwner("john");
			model.setName("John's second adaptation model");
			model.setClassname("john.SecondStrategy");
			model.setFilename("john/SecondStrategy.java");
			model.setCode(Files.readString(Paths.get(resourceLoader.getResource("classpath:" + model.getFilename()).getURI())));
			model = repository.save(model);
			log.info("Preloading " + model);

			sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess("Did not meet criteria", false);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(20)));
			benchmarks.save(sim);
			log.info("Preloading " + sim);
			
			sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess(BenchmarkRunner.INSTANCE.runBenchmarkNow(model).getResult(), true);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(15)));
			benchmarks.save(sim);
			log.info("Preloading " + sim);

			model = new AdaptationModel();
			model.setOwner("mary");
			model.setName("Mary's first adaptation model");
			model.setClassname("mary.FirstStrategy");
			model.setFilename("mary/FirstStrategy.java");
			model.setCode(Files.readString(Paths.get(resourceLoader.getResource("classpath:" + model.getFilename()).getURI())));
			model = repository.save(model);
			log.info("Preloading " + model);
						
			sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess(BenchmarkRunner.INSTANCE.runBenchmarkNow(model).getResult(), true);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(10)));
			benchmarks.save(sim);
			log.info("Preloading " + sim);
			
			sim = new HWBenchmark();
			sim.setAdaptationModelId(model.getId());
			sim.setResultAndSuccess(BenchmarkRunner.INSTANCE.runBenchmarkNow(model).getResult(), true);
			sim.setStartedAt(Instant.now().minus(Duration.ofHours(5)));
			benchmarks.save(sim);
			log.info("Preloading " + sim);
		};
	}
}