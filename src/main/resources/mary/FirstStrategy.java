package mary;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Effector;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Probe;

public class FirstStrategy implements AdaptationAPI {

	public FirstStrategy() {
	}
	
	@Override
	public String run(Probe probe, Effector effector) {
		return "Results from Mary's first strategy.";
	}
}
