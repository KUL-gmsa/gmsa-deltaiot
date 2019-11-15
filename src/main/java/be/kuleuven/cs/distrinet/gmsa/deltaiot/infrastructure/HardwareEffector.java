package be.kuleuven.cs.distrinet.gmsa.deltaiot.infrastructure;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Effector;

/**
 * This represents the effector that accesses the real hardware.
 */
public class HardwareEffector implements Effector {

	public static final Effector INSTANCE = new HardwareEffector();
	
	private HardwareEffector() {
	}

	@Override
	public void perform(EffectorCommand command) {
		// Here goes to the code to send commands to the hardware
		System.out.println("Managed system performing command " + command);
	}

}
