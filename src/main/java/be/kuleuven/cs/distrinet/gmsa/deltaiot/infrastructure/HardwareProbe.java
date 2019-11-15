package be.kuleuven.cs.distrinet.gmsa.deltaiot.infrastructure;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Probe;

/**
 * This represents the probe that yields actual information from the hardware.
 */
public class HardwareProbe implements Probe {

	public static final Probe INSTANCE = new HardwareProbe();
	
	private HardwareProbe() {
		
	}

	@Override
	public ProbeResult probe() {
		// Here goes the code to access the hardware to obtain measurements
		return new ProbeResult();
	}

}
