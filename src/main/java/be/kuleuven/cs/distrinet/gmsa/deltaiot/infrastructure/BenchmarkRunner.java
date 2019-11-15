package be.kuleuven.cs.distrinet.gmsa.deltaiot.infrastructure;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.AdaptationModel;

public class BenchmarkRunner {

	public static final BenchmarkRunner INSTANCE = new BenchmarkRunner();

	public static class BenchmarkOutcome {
		private boolean success;
		private String result;

		public BenchmarkOutcome(String result, boolean success) {
			this.success = success;
			this.result = result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getResult() {
			return result;
		}

		public boolean isSuccess() {
			return success;
		}
	}

	private static class RunBenchmarkTask implements Runnable {
		private final Consumer<BenchmarkOutcome> outcomeConsumer;
		private final AdaptationModel model;

		public RunBenchmarkTask(AdaptationModel model, Consumer<BenchmarkOutcome> outcomeConsumer) {
			this.model = model;
			this.outcomeConsumer = outcomeConsumer;
		}

		@Override
		public void run() {
			var outcome = INSTANCE.runBenchmarkNow(model);
			outcomeConsumer.accept(outcome);
		}
	}

	private BenchmarkRunner() {
	}

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private final Semaphore benchmarkExecutionLock = new Semaphore(1);

	public void scheduleBenchmark(AdaptationModel model, Consumer<BenchmarkOutcome> outcomeCallback) {
		// Random is used to emulate that it may take a while before the simulation
		// starts
		executorService.schedule(new RunBenchmarkTask(model, outcomeCallback), new Random().nextInt(30), TimeUnit.SECONDS);
	}
	
	public BenchmarkOutcome runBenchmarkNow(AdaptationModel model) {
		if (!benchmarkExecutionLock.tryAcquire()) {
			throw new IllegalStateException("Multiple benchmarks are trying to run at the same time.");
		}
		try {
			System.out.println("Running benchmark for model " + model);
			var modelInstance = model.compileAndCreateInstance();
			return new BenchmarkOutcome(modelInstance.run(HardwareProbe.INSTANCE, HardwareEffector.INSTANCE), true);
		} catch (Throwable e) {
			e.printStackTrace();
			return new BenchmarkOutcome(e.getClass().getTypeName() + (e.getMessage() != null ? ": " + e.getMessage() : null), false);
		} finally {
			benchmarkExecutionLock.release();
		}
	}
}
