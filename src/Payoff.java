import java.util.Arrays;


public class Payoff {
	
	public Payoff(){
		
	}
	
	//Computes a simple payoff based on one decision
	public double computeSimplePayoff(double score, int outcome, int currentDecision){
		
		if(outcome == currentDecision){
			return ++score;
		}
		else
			return --score;
		
	}
	
	//Computes payoff for multiple resources based on how many resoures are needed to consider a win
	public double computeMultipleResourcesPayoff(double score, int[] outcomes, int[] decisions, int numberOfResourceWinsNeeded){

		int count = 0;

		for (int i = 0; i < outcomes.length; i++) {

			if (outcomes[i] == decisions[i]) {
				count++;
			}
		}

		if (count >= numberOfResourceWinsNeeded) {
			return ++score;
		} else
			return --score;
		
	}

}
