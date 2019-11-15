package john;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Effector;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI.Probe;

public class SecondStrategy implements AdaptationAPI {

	public SecondStrategy() {
	}
	
	@Override
	public String run(Probe probe, Effector effector) {
		return "Results from John's second strategy.";
	}
}
