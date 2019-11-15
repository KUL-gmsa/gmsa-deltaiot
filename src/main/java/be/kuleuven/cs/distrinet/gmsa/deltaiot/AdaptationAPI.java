package be.kuleuven.cs.distrinet.gmsa.deltaiot;

/**
 * This API must be implemented by all adaptation strategies that are uploaded
 * to the system.
 */
public interface AdaptationAPI {

	/**
	 * Interface for a probe that yields information about the managed system.
	 */
	public static interface Probe {
		public static class ProbeResult {

		}

		public ProbeResult probe();
	}

	/**
	 * Interface for an effector that updates the configuration of the managed
	 * system.
	 */
	public static interface Effector {
		public static class EffectorCommand {

		}

		public void perform(EffectorCommand command);
	}

	/**
	 * 
	 * @return The result of running the adaptation. For simplicity, this is just
	 *         modeled as a String.
	 */
	public String run(Probe probe, Effector effector);
}
