import java.util.Random;

public class Main {

	public static void testVarianceRegularMG(MinorityGame mg, int memoryIterations, boolean separateScoringChoice, int numberOfResourceWinsNeeded, double[] capacityPercent){
		
		for (int i = 0; i < memoryIterations; i++) {

			mg.playGame(i + 1, false, numberOfResourceWinsNeeded, capacityPercent);

		}
		
		mg.plotVarianceAlpha();
		mg.printVariance();
		mg.printAlpha();
		
		System.out.println("");
		
	}
	
	
	
	public static void main(String[] args) {
		
		int numberOfAgents = 501;
		int numberOfResources = 3;
		int numberOfResourceWinsNeeded = 3;
		boolean separateScoingChoice = true;
		double[] capacityPercent = {.33,.33,.33};
		
		int memorySize = 4;
		int numberOfStrategies = 3;
		
		int numberOfDecisions = 2;
		int numberOfIterartions = 1000;
		int memoryIterations = 11;
		int threshold = -4;
		double range = .01;

		MinorityGame mg = new MinorityGame(numberOfAgents, memorySize,
				numberOfDecisions, numberOfIterartions, numberOfStrategies,
				memoryIterations, numberOfResources);

		//mg.memorySizePlot();
		mg.playGame(memorySize, separateScoingChoice, numberOfResourceWinsNeeded, capacityPercent);
		//mg.playEvolutionaryGame(memorySize, numberOfResourceWinsNeeded, capacityPercent, threshold, range);
		
		//lifetime
		//for(int i = 1; i < memoryIterations; i++){
		
//			mg.initializeLifeTimeArray();
//		for(int j = 0; j < 10; j++){
//			mg.playEvolutionaryGame((memorySize), numberOfResourceWinsNeeded, capacityPercent, threshold, range);
//		}
//		mg.plotLifeTimeEMG();
//		mg.plotAverageProbabilityUse();
		
		//}
		
//		for(int i = 0; i < memoryIterations; i++){
//			
//			mg.playEvolutionaryGame(i + 1, numberOfResourceWinsNeeded, capacityPercent, threshold, range);
//		
//		}
//		
//		mg.plotVarianceAlpha();
//		mg.printVariance();
//		mg.printAlpha();
//		
	//	testVarianceRegularMG(mg, memoryIterations, separateScoingChoice, numberOfResourceWinsNeeded, capacityPercent);
//		for (int i = 0; i < memoryIterations; i++) {
//
//			mg.playGame(i + 1, false, numberOfResourceWinsNeeded, capacityPercent);
//
//		}

//		Random rnd = new Random();
//		double random1 = rnd.nextDouble();
//		double temp = Math.floor(random1 * 100000);
//		//double temp2 = temp * 100;
//		System.out.println(random1+ " | " + temp);
//		for(double i = 0; i < 1;){
//			System.out.println(i);
//			 i += 0.001;
//		}
		
//		for(int i = 1; i < 20; i++){
//			System.out.println(i * 5000);
//		}
//
		
	}

} 
