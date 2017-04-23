import java.util.Random;

/**
 * 
 * @author dromero
 *
 */
public class MinorityGame {
	//game class holds agents
	private int numOfAgents;//number of agents/players
	private Agent[] agents;//array of agents
	private int[] memory; //the bits used to decide a strategy
	private int iterations;//the number of times the game is played
	private double[] attendanceArr; //stores the attendance from each round
	private int currentOutcome;//the outcome of an iterations
	private int[] outcomes;//outcomes for each resource
	private int[][] resourceAttendanceMatrix;
	private int currentMemoryValue;//the current memory decimal value
	private int numOfDecisions;//the number of decisions a player can make
	//private int attendance; //stores the number of ones accumulated from the player's strategy
	private Payoff myPayoff;//payoff object to calculate scores
	private int memorySize;//each agent is given a set of S strategies randomly drawn from the total 2^2^m(strategiesPower) possible strategies.

	private double[] agentsIndex;
	private double[] winningRate;
	private double[] iterArray;
	private int numOfStrategies;
	private int memoryIterations;
	private double[][] varianceMatrixForAlpha;
	private double[] varianceArrForAlpha;
	private double[] alphaArr;//Used for plotting alpha
	
	//Multi-resources
	private Resource[] resources;//Holds resource objects
	private int numOfResources;//Indicates how many resources will be used in the game
	private double[] successfulResourceUsageArray;//for agents
	private double[] resourceUsage;//for resources
	private int numberOfResourceWinsNeeded;//Decides how many resources/scenarios an agent needs win in order to win overall 
	private boolean separateStrategyScoring = false;//Used to score different strategies for different resources
	
	//EMG
	private int threshold;//Threshold for Evolutionary MG
	private double range;//Range for Evloutionary MG
	private Strategy globalStrategy;//Evolutionary MG
	private int globalChoice;//Evloutionary MG

	
	//EMG L(p) and P(p) data members
	private double[] probabilityUsageArray;//stores how many times a probability is used in a simulation of T iterations
	private double[] probabilitySwitchRateArray;//stores how many times a probability is switched from being used to not used
	private double[] probabilityIndexArray;// divides 1 into however many pieces
	private int probUseArrSize = 100;//the size of the probabilityIndexArray
	private int lifeTimeRuns = 10; //number of simulations ran with T iterations, used to get the average L(p) and P(p) for EMG
	private double[] lifeTimeArray;//stores the L(p) value for each simulation
	private double[] frequencyDistArray;//stores the P(p) values for the last 10%
	private double[] finalAverageProbabilityUseArray;//final P(p) values over lifeTimeRuns
	
	//Standard Deviation
	private double[][] pointToPointStandardDeviationMatrix; //Used for storing 10% of iterations for point to point standard deviation for multiple resources
	private int pointToPoint;// used to determine the amount of segments iterations are divided into 
	private double[] pointToPointMeans;//calculates the mean for segments of iterations
	private double stanDev[]; //used to graph the standard deviation for a resource
	

	//Minority Game constructor
	public MinorityGame(int numOfAgents, int memorySize, int numOfDecisions, int iterations, int numOfStrategies, int memoryIterations, int numOfResources){
		
		//attendance = 0;
		this.iterations = iterations;
		this.numOfAgents = numOfAgents;
		this.memorySize = memorySize;
		this.numOfDecisions = numOfDecisions;
		this.numOfStrategies = numOfStrategies;
		this.memoryIterations = memoryIterations;
		this.numOfResources = numOfResources;
		pointToPoint = 20;
		
		initializepointToPointStandardDeviationMatrix();
		initializepointToPointMeanArray();
		initializeVarianceAlphaArray();
		initializeAlphaArray();
		initializeVarianceAlphaMatrix();

	}
	
	//Initializes all of the parameters for a new game
	public void initializeGame(double[] resourceCapacityDenominator){
		
		initializeResources(resourceCapacityDenominator);
		initializeMemory();
		initializePlayers(numOfStrategies, memorySize);
		
		agentsIndex = new double[numOfAgents];
		attendanceArr = new double[iterations];
		resourceUsage = new double[iterations];
		winningRate = new double[numOfAgents];
		myPayoff = new Payoff();
		
		initializeIterArray();
		initializeAgentPlot();
		initializeResourceAttendanceMatrix();
		intializeOutcomesArr();
		initializeSuccessfulResourceUsageArray();
		initializepointToPointStandardDeviationMatrix();
		
	}
	
	//Initializes the Evolutionary Minority Game
	public void initializeEvolutionaryMinorityGame(double[] resourceCapacityDenominator, int threshold, double range){
		
		this.threshold = threshold;
		this.range = range;
		
		initializeResources(resourceCapacityDenominator);
		initializeMemory();
		createGlobalStrategy(memorySize, numOfResources);
		initializePlayers();
		
	
		
		agentsIndex = new double[numOfAgents];
		attendanceArr = new double[iterations];
		resourceUsage = new double[iterations];
		winningRate = new double[numOfAgents];
		myPayoff = new Payoff();
		
		
		initializeProbabilityArray();
		initializeIterArray();
		initializeAgentPlot();
		initializeResourceAttendanceMatrix();
		intializeOutcomesArr();
		initializeSuccessfulResourceUsageArray();
		
	}
	
	//Creates a global strategy for the Evolutionary Minority Game
	public void createGlobalStrategy(int memsize, int numOfResources) {
		
		globalStrategy = new Strategy(memsize, numOfResources);
		
	}
	
	/* this creates the number of agents with the number of strategies and memory size passed in*/
	public void initializePlayers(int numOfStrategies, int memorySize) {

		agents = new Agent[numOfAgents];

		for (int i = 0; i < numOfAgents; i++) {
			
			agents[i] = new Agent(numOfStrategies, memorySize, numOfResources);

		}

	}
	
	//Evolutionary Game, players don't need strategies
	public void initializePlayers() {

		agents = new Agent[numOfAgents];

		for (int i = 0; i < numOfAgents; i++) {
			
			agents[i] = new Agent(numOfResources, probUseArrSize);

		}

	}
	
	//Initializes L(p) array for average for X amount of simulations ran. 
	public void initializeLifeTimeArray(){
		
		lifeTimeArray = new double[probUseArrSize];
		finalAverageProbabilityUseArray = new double[probUseArrSize];
		
		
		for(int i = 0; i < probUseArrSize; i++){
			lifeTimeArray[i] = 0;
			
			finalAverageProbabilityUseArray[i] = 0;
			
		}
	}
	
	//Initializes point to point standard deviation matric for multiple resources
	public void initializepointToPointStandardDeviationMatrix(){
		
		pointToPointStandardDeviationMatrix = new double[numOfResources][pointToPoint];
		
		for(int i = 0; i < numOfResources; i++){
			for(int j = 0; j < pointToPoint; j++){
			
			pointToPointStandardDeviationMatrix[i][j] = 0;
			
			}
		}
		
	}
	
	//Initializes the array of means used for the point to point standard deviation calculation
	public void initializepointToPointMeanArray(){
		
		pointToPointMeans = new double[pointToPoint];
		stanDev = new double[pointToPoint];
		
		for(int i = 0; i < pointToPoint; i++){
	
			pointToPointMeans[i] = 0;
			stanDev[i] = 0;
		}
		
	}
	
	//Initializes the arrays necessary to calculate the L(p) and P(p) functions for the EMG
	public void initializeProbabilityArray(){
		
		probabilityUsageArray = new double[probUseArrSize];
		probabilityIndexArray = new double[probUseArrSize];
		probabilitySwitchRateArray = new double[probUseArrSize];
		frequencyDistArray = new double[probUseArrSize];


		
		for(int i = 0; i < probabilityUsageArray.length; i++){
			
			probabilityUsageArray[i] = 0;
			probabilitySwitchRateArray[i] = 0;
			frequencyDistArray[i] = 0;
			double temp = (double)(i+1) / (double)probUseArrSize;
			probabilityIndexArray[i] = temp; 
			System.out.println(temp);
			
		}
			
	}
	
	//Initialize the size of memory used for making decisions/ history of moves for each resource
	public void initializeMemory() {

		memory = new int[memorySize];
		//memoryMatrix = new int[memorySize][numOfResources];

		for (int j = 0; j < numOfResources; j++) {
			
			for (int i = 0; i < memory.length; i++) {

				memory[i] = randomInt(numOfDecisions);
			//	memoryMatrix[j][i] = memory[i];
				
			}
			
			resources[j].setBinaryMemory(memory);

		}
	}
	
	
	///*returns a random number from 0 to n-1*/
	public int randomInt(int numOfDecisions){
			
			Random rnd = new Random();
			int random1 = rnd.nextInt(numOfDecisions);
			return random1;
		}
	
	//sets the memory size
	public void setMemorySize(int m){
		
		memorySize = m;
	}
	
	//Initializes the resources array with the appropriate capacity values
	public void initializeResources(double[] resourceCapacityDenominator) {

		resources = new Resource[numOfResources];
		
		for (int i = 0; i < numOfResources; i++) {

			double capacity = Math.floor((numOfAgents * resourceCapacityDenominator[i]));

			resources[i] = new Resource(capacity, memorySize);
		}

	}
		
	//Used for plotting the outcomes
	public void initializeIterArray(){
		
		iterArray = new double[iterations];

		for(int i = 0; i < iterations; i++){
			iterArray[i] = i + 1;
		}
	}
	
	// this is for plotting the number of agents in the winning rate plot
	public void initializeAgentPlot() {

		for (int i = 0; i < agentsIndex.length; i++) {
			agentsIndex[i] = i + 1;
		}
	}

	// converts the memory from an array to an integer
	public int convertMemory() {

		String binaryString = "";
		int base = 2;

		for (int i = 0; i < memory.length; i++) {
			binaryString += memory[i];
		}
		return Integer.parseInt(binaryString, base);
	}

	// sets the binary memory in the game from each resource
	public void setBinaryMemory(int[] mem) {

		for (int i = 0; i < mem.length; i++) {

			memory[i] = mem[i];
		}

	}

	// initializes the array for each resource attendance in a round
	public void intializeOutcomesArr() {
		outcomes = new int[numOfResources];

		for (int i = 0; i < numOfResources; i++) {
			outcomes[i] = 0;
		}
	}

	// initializes the MATRIX for the results of all attendance for each resource
	public void initializeResourceAttendanceMatrix() {

		resourceAttendanceMatrix = new int[numOfResources][iterations];

		for (int i = 0; i < numOfResources; i++) {
			for (int j = 0; j < iterations; j++) {
				resourceAttendanceMatrix[i][j] = 0;
			}
		}

	}
	
	public void playEvolutionaryGame(int memSize, int numberOfResourceWinsNeeded, double[] resourceCapacityDenominator, int threshold, double range){
		
		setMemorySize(memSize);
		initializeEvolutionaryMinorityGame(resourceCapacityDenominator, threshold, range);
		this.numberOfResourceWinsNeeded = numberOfResourceWinsNeeded;
		int globalChoice = 0;
		
		for(int i = 0; i < iterations; i++){
			//convert binary to decimal
			//int j;
			for (int j = 0; j < numOfResources; j++){
				
			setBinaryMemory(resources[j].getBinaryMemory());
			
			currentMemoryValue = convertMemory();
			globalChoice = globalStrategy.getValue(currentMemoryValue);
//			System.out.println();
//			System.out.println("Round: " + (i+1) + " | Memory Decimal Value: " + currentMemoryValue + " | Global Choice: " + globalChoice + " | Resource number: " + (j + 1) + " | Capacity: " + resources[j].getCapacity()+ " Memory size: " + memorySize); //testing purposes
//			//reset attendance
			int attendance = 0;
			
				for (int k = 0; k < agents.length; k++) {
					// agents make decisions based on the memory value and their
					// equivalent index inside the strategy with the best score
					if (agents[k].makeDecision(globalChoice, j) == 1) {

						attendance++;
					}
				}

				// attendanceArr[i] = attendance;
				resourceAttendanceMatrix[j][i] = attendance;
				// apply minority rule to get the outcome
				// minorityRule();
				outcomes[j] = minorityRule(attendance, j);
				currentOutcome = outcomes[j];
				resources[j].updateMemory(outcomes[j]);
//				System.out.println("Attendance count: " + attendance);
//				System.out.println("Winning Decision: " + outcomes[j]);
//				System.out.println();
//				
				for(int a = 0; a < agents.length; a++){
				
				agents[a].incrementWinCount(outcomes[j], j);
				
				if(currentOutcome == 1 && agents[a].getDecision(j) == 1 )
					agents[a].incrementSuccessfulResourceUsage(j);

			}
			}
				
				//apply payoffs
				for(int k = 0; k < agents.length; k++){
					
//					agents[k].setScore(myPayoff.computeSimplePayoff(agents[k].getScore(), currentOutcome, agents[k].getDecision()));
//					
//				
//					agents[k].simpleWinCount(currentOutcome);
					agents[k].setScore(myPayoff.computeMultipleResourcesPayoff(agents[k].getScore(), outcomes, agents[k].getDecisions(),numberOfResourceWinsNeeded));
					
					agents[k].finalWinCount(numberOfResourceWinsNeeded);
					
					//agents[k].printValues();
				//	System.out.println("   Agent: " + (k + 1 ) + " | Decision: " + agents[k].getDecision() + " | Gene(P): " + agents[k].getProbability() + " | Random double used for decision: " + agents[k].getRandomDoubleStoredValue() +" | Agent Score: " + agents[k].getScore() + " | Player win count: " + agents[k].getWinCount());
					
					agents[k].checkThreshold(threshold, range);
					
//					L(p) - THe lifetime of p. This keeps track of how frequently a specific range of p is used and when it is modified. 
					//Has it been modified since the last iteration?
					if(agents[k].getOldProbability() != agents[k].getProbability()){
						
						double oldProb = Math.floor(agents[k].getOldProbability() * probUseArrSize);
						int index = (int)oldProb;
						probabilitySwitchRateArray[index] += 1;
						
					}
					
					//Increment the probability's count
					double prob = Math.floor(agents[k].getProbability() * probUseArrSize);
					int index = (int)prob;
					probabilityUsageArray[index] += 1;
					
					if(i >= (iterations - (iterations / 10))){

						frequencyDistArray[index] += 1;
						
					}
//					//Keeps track if an agent uses a probability in a simulation of T iterations
//					if(agents[k].getProbabilityUseValue(index) == 0){
//						agents[k].setProbabilityUseValue(index);
//						
//					}
//					}
				
					
				}

			}
		
		plotAttendance();
		plotResourceUsage();
		plotWinningRate();
		
//		calculatePointToPointStandardDeviation();
//		plotPointToPointStandardDeviation();

		
	//	calculateEMG_LandPFunction();
		
		
//		calculateAlpha();
//		calculateNormalizedVarianceForAlpha(memorySize);
//		printVariance();
		
		//calculateResourceUsageForAgents();
	}
	
	
	
	
	//regular minority game
	public void playGame(int memSize, boolean separateStrategiesChoice, int numberOfResourceWinsNeeded, double[] resourceCapacityDenominator){
		
		setMemorySize(memSize);
		initializeGame(resourceCapacityDenominator);
		//int count = 0;
		separateStrategyScoring = separateStrategiesChoice;
		this.numberOfResourceWinsNeeded = numberOfResourceWinsNeeded;
		
		for(int i = 0; i < iterations; i++){
			//convert binary to decimal
			//int j;
			
			if(i == 500){
				
				System.out.println("");
			}
			for (int j = 0; j < numOfResources; j++){
				if (j == 1 && i == 500){
					int whatever = 3;
				}
			setBinaryMemory(resources[j].getBinaryMemory());
			
			currentMemoryValue = convertMemory();
			//System.out.println();
			//System.out.println("Round: " + (i+1) + " | Memory Decimal Value: " + currentMemoryValue + " | Resource number: " + (j + 1) + " | Capacity: " + resources[j].getCapacity()+ " Memory size: " + memorySize); //testing purposes
			//reset attendance
			int attendance = 0;
			
				for (int k = 0; k < agents.length; k++) {
					// agents make decisions based on the memory value and their
					// equivalent index inside the strategy with the best score
					if (agents[k].makeDecision(currentMemoryValue, j, separateStrategyScoring) == 1) {

						attendance++;
					}
				}

				// attendanceArr[i] = attendance;
				resourceAttendanceMatrix[j][i] = attendance;
				// apply minority rule to get the outcome
				// minorityRule();
				outcomes[j] = minorityRule(attendance, j);
				currentOutcome = outcomes[j];
				
					
				for(int a = 0; a < agents.length; a++){
					
					agents[a].incrementWinCount(outcomes[j], j);
					if(currentOutcome == 1 && agents[a].getDecision(j) == 1 )
						agents[a].incrementSuccessfulResourceUsage(j);
					//different score for different strategies for different resources
					if(separateStrategyScoring)
						agents[a].setStrategyScore(myPayoff.computeSimplePayoff(agents[a].getStrategyScore(j), currentOutcome, agents[a].getDecision()), j);
				}
				
//				for(int b = 0; b < agents.length; b++){
//					
//					
//
//					System.out.print("Agent: " + (b + 1));
//					agents[b].printValues();
//					System.out.print(" | Strategy Score: "
//							+ agents[b].getStrategyScore(j));
//					System.out.println();
//
//				}
				
				//System.out.println("Attendance count: " + attendance);
				//System.out.println("Winning Decision: " + outcomes[j]);
				//System.out.println();
				
				resources[j].updateMemory(outcomes[j]);

			}
			//apply the payoffs
			for(int k = 0; k < agents.length; k++){
				
				//compute simple payoff each resource at a time, agents only need to win one resource to increment 
				//multiple resources, must win all resources to increment score and win count
				//agents[k].setScore(myPayoff.computeMultipleResourcesPayoff(agents[k].getScore(), outcomes, agents[k].getDecision()));
				//agents[k].setStrategyScore(myPayoff.computeMultipleResourcesPayoff(agents[k].getStrategyScore(), outcomes, agents[k].getDecision()));
//				agents[k].incrementMultipleResourcesWinCount(outcomes);
				
				//compute simple payoff each resource at a time, agents only need to win one resource to increment 
				if(separateStrategyScoring == false)
					agents[k].setStrategyScore(myPayoff.computeMultipleResourcesPayoff(agents[k].getStrategyScore(), outcomes, agents[k].getDecisions(), numberOfResourceWinsNeeded));
				
				agents[k].setScore(myPayoff.computeMultipleResourcesPayoff(agents[k].getScore(), outcomes, agents[k].getDecisions(),numberOfResourceWinsNeeded));
				
				agents[k].finalWinCount(numberOfResourceWinsNeeded);
				//testing
				//agents[k].printValues();
				
//				System.out.print("   Agent: " + (k + 1 ) );//+  " | Player win count: " + agents[k].getWinCount());
//				agents[k].printValues();
//				//print test for different scoring for different strategies for different resources
//				System.out.print("| Strategy score: " + agents[k].getStrategyScore() + " | Player Score: " + agents[k].getScore() + " | Player win count: " + agents[k].getWinCount());
//				System.out.println();
//				
		}

			
			
		
			
			
//			
//			System.out.println("Winning Decision: " + currentOutcome);
//			System.out.println();
//			
			//update binary memory value
			
//			for(int m = 0; m < agents.length; m++){
//				if(agents[m].finalWinCount(numOfResources)){
//					System.out.println("Agent " + (m+1) + " is a winner winner chicken dinner!");
//				}
//				agents[m].setWinCount(0);
//			}
			
		}

		plotAttendance();
		plotResourceUsage();
		plotWinningRate();
		
//		calculatePointToPointStandardDeviation();
//		plotPointToPointStandardDeviation();
		
		//plotResourceUsageForAgents();
		
		
//		calculateAlpha();
//		calculateNormalizedVarianceForAlpha(memorySize);
//		printVariance();
		
		System.out.println("");
		
		}
	
	//Returns the winning group based on if it's the minority choice
	public int minorityRule(int attendance, int resourceIndex) {

		if (attendance > resources[resourceIndex].getCapacity())
			return 0;
		else
			return 1;
		
	}
	
	//--------------------------------------------------------------------------------------------------------- Plots ----------------------------------------
	//Plots the successful usage of a resource
	public void plotResourceUsage(){
		
		System.out.println("------------------------------Resource Usage---------------------------");
		
		for (int i = 0; i < numOfResources; i++) {
			
			double resourceUsageCount = 0;
			System.out.println("---------------------------------------RESOURCE USAGE: RESOURCE NUMBER: " + (i+1));
			
			for (int j = 0; j < iterations; j++) {


				if (resourceAttendanceMatrix[i][j] <= resources[i].getCapacity()) {
					
					resourceUsage[j] = resourceAttendanceMatrix[i][j];
					
					resourceUsageCount++;
					
				} else {
					
					resourceUsage[j] = 0;
					
				}
				
				System.out.println(resourceUsage[j]);
				
			}
			
			Plot resourceUsagePlot = new Plot("Resource Usage: MemorySize: " + memorySize + " | Number of Agents: " + numOfAgents + " | Number of Strategies: " + numOfStrategies + 
					" | Iterations: " + iterations + " | Resource Number: "  + (i + 1) + " | Capacity: " + resources[i].getCapacity() + " | Separate Strategy Scoring: " + separateStrategyScoring + 
					" | Resource Success Usage: " + ((resourceUsageCount / iterations) * 100) + "%", iterArray , resourceUsage);
		}
	}

	//Plots the attendance of a resource
	public void plotAttendance(){
		System.out.println("------------------------------ATTENDANCE---------------------------");
		
		for(int i = 0; i < numOfResources; i++){
			
			System.out.println("---------------------------------------ATTENDANCE: RESOURCE NUMBER: " + (i+1));
			
			for(int j = 0; j < iterations; j++){
				
			attendanceArr[j] = resourceAttendanceMatrix[i][j];
			
			System.out.println(attendanceArr[j]);
			
			//System.out.println("Attendance for iteration: " + (j+1) + " = " + attendanceArr[j] + ", ");
			}
			
			Plot outcomesPlot = new Plot("Outcomes with: MemorySize: " + memorySize + " | Number of Agents: " + numOfAgents + " | Number of Strategies: " + numOfStrategies + 
					" | Iterations: " + iterations + " | Resource Number: "  + (i + 1) + " | Capacity: " + resources[i].getCapacity() + 
					" | Resources needed to win: " + numberOfResourceWinsNeeded + " | Separate Strategy Scoring: " + separateStrategyScoring, iterArray , attendanceArr);
		
		}
	}
	
	//Calculates the winning rate and places in an array for plotting purposes
	public void plotWinningRate(){
		
		double winRateAverage = 0;
		int loseCount = 0;
		
		System.out.println("WINNING RATE------------------------------------------------------------------------------------------------------------------------");
		
		for(int i = 0; i < agents.length; i++){
			
			winningRate[i] = agents[i].calculateWinRate(iterations);
			winRateAverage += winningRate[i];
			System.out.println(winningRate[i]);
			
			if(winningRate[i] == 0){
				loseCount++;
			}
		//	System.out.println("Winning Rate for agent: " + (i+1) + " = " + winningRate[i] + ", ");
		}
		
		
		Plot winningRatePlot = new Plot("Winning Rate! MemorySize: " + memorySize + " | Number of Agents: " + numOfAgents + " | Number of Strategies: " + numOfStrategies + 
				" | Iterations: " + iterations + " | Number of Resources: " +  numOfResources + " | Resources needed to win: " + 
				numberOfResourceWinsNeeded + " | Separate Strategy Scoring: " + separateStrategyScoring + " | Average winning rate: " + (winRateAverage / numOfAgents) + " | Agents with no wins: " + loseCount, agentsIndex, winningRate);

	}
	
	//--------------------------------------------------------------------------------------------------------- Variance & Alpha ----------------------------------------

	//Creates variance array
	public void initializeVarianceAlphaArray() {

		varianceArrForAlpha = new double[memoryIterations];

		for (int i = 0; i < memoryIterations; i++) {
			varianceArrForAlpha[i] = i;
		}

	}
	
	//Creates the variance matrix for all of the resources.
	public void initializeVarianceAlphaMatrix() {
		
		varianceMatrixForAlpha = new double[numOfResources][memoryIterations];

		for (int r = 0; r < numOfResources; r++) {
			
			for (int i = 0; i < memoryIterations; i++) {
				
				varianceMatrixForAlpha[r][i] = i;
			}
		}

	}
	
	//Calculates the actual mean of the attendance of the designated resource.
	public double calculateMean(int resourceIndex){
		
		double mean = 0; 
		
		for(int i = 0; i < iterations; i++){
			
			mean += resourceAttendanceMatrix[resourceIndex][i];
		}
		mean = mean/iterations;
		
		return mean;
	}
	
	//Calculates the mean for point to point variance.
	public void calculatePointToPointMean(int resourceIndex) {

		double mean = 0;
		int count = 1;
		
		for (int i = 0; i < iterations; i++) {

			
			
			mean += resourceAttendanceMatrix[resourceIndex][i];
			
			if((i+1) == (iterations / pointToPoint) * count){
				
				pointToPointMeans[count - 1] = mean / ((double)iterations / pointToPoint);
				mean = 0;
				count++;
			}
		}

	}
	
	//Calculates and normalizes the variance of all of the iterations of one game with all resources.
	//memSize represents the current memory size in the memory iterations. It's used to calculate the variance from memory = 1-14. 
	//These numbers are stored in a matrix for each resource used for plotting. 
	public void calculateNormalizedVarianceForAlpha(int memSize) {

		for (int r = 0; r < numOfResources; r++) {
			
			double mean = calculateMean(r);//resources[r].getCapacity();//
			
			double variance = 0;
			
			for (int i = 0; i < iterations; i++) {

				variance += (Math.pow(resourceAttendanceMatrix[r][i] - mean, 2));

			}

			variance = (variance / iterations) / numOfAgents;
			//System.out.println(variance);
			varianceMatrixForAlpha[r][memSize - 1] = variance;
		}
	}

	
	//Calculates the point to point variance
	public void calculatePointToPointStandardDeviation(){
		
		for (int r = 0; r < numOfResources; r++) {
		
		calculatePointToPointMean(r);//resources[r].getCapacity();//
		
		double variance = 0;
		int count = 1;
		
		for (int i = 0; i < iterations; i++) {
			
			variance += (Math.pow((resourceAttendanceMatrix[r][i] - pointToPointMeans[count - 1]), 2));
			
				
				if((i+1) == (iterations / pointToPoint) * count){
			
				pointToPointStandardDeviationMatrix[r][count - 1] = Math.sqrt((variance / ((double)iterations / pointToPoint)));
				variance = 0;
				count++;
			}
		}
		}
	}
	
	//Prints the variance for each memory size for testing purposes
	public void printVariance() {

		for (int i = 0; i < numOfResources; i++) {

			System.out.println("Resource: " + (i + 1));

			for (int j = 0; j < memoryIterations; j++) {

				System.out.println(varianceMatrixForAlpha[i][j]);

			}
		}
	}
	
	
	//Plots the point to point standard deviation 
	public void plotPointToPointStandardDeviation() {

		double[] count = new double[pointToPoint];

		for (int i = 0; i < numOfResources; i++) {
				System.out.println("Resource: " + (i+1) + " standard deviation ---------------------------------------------------------------------------------------------------------------");
			for (int j = 0; j < pointToPoint; j++) {

				stanDev[j] = pointToPointStandardDeviationMatrix[i][j];
				System.out.println(pointToPointStandardDeviationMatrix[i][j]);
				count[j] = (j+1) * (iterations / pointToPoint);
			}
		

			Plot pointToPointStandardDeviation = new Plot("Point to point standard deviation: N: " + numOfAgents + " Memory size: 1 - 14"  + " Strategies: " + numOfStrategies + " | Iterations: " + iterations + " | Resource Number: "  + (i + 1) + " Capacity: " + resources[i].getCapacity(), count, stanDev);// + " | Range: " + range + " | Threshold: " + threshold
		}

	}
	
	
	//Plots the Variance with respect to alpha. alpha = (2 ^ memSize) / number of agents
	public void plotVarianceAlpha() {

		for (int i = 0; i < numOfResources; i++) {

			for (int j = 0; j < memoryIterations; j++) { 
				
				varianceArrForAlpha[j] = varianceMatrixForAlpha[i][j];
			}
			
			Plot varianceAlphaPlot = new Plot("Variance with respect to Alpha: N: " + numOfAgents + " Memory size: 1 - 14"  + " Strategies: " + numOfStrategies + " | Iterations: " + iterations + " | Resource Number: "  + (i + 1) + " Capacity: " + resources[i].getCapacity(), alphaArr, varianceArrForAlpha);// + " | Range: " + range + " | Threshold: " + threshold

		}
	}

	//Creates the alpha array used for plotting 
	public void initializeAlphaArray(){
		alphaArr = new double[memoryIterations];
		
		for(int i = 0; i < memoryIterations; i++){
			alphaArr[i] = i;
		}
	}
	
	//Calculates alpha for each game to compare with the variance
	//alpha = (2 ^ memSize) / number of agent
	public void calculateAlpha(){
		double alpha = 0;
		alpha = (Math.pow(2, memorySize)) / numOfAgents;
		alphaArr[memorySize - 1] = alpha;
	}
	
	//Prints alpha for testing purposes
	public void printAlpha() {
		
		System.out.println("Alpha");
		
		for (int i = 0; i < memoryIterations; i++) {
			
			System.out.println(alphaArr[i]);
		}
	}

	//--------------------------------------------------------------------------------------------------------- Resource Usage with Respect to Agents ----------------------------------------

	
	//Creates the array to hold an agents successfulness of using each resource
	public void initializeSuccessfulResourceUsageArray() {

		successfulResourceUsageArray = new double[numOfAgents];

		for (int i = 0; i < numOfAgents; i++) {

			successfulResourceUsageArray[i] = 0;

		}
	}
	
	//Calculates successful resource usage
	public void plotResourceUsageForAgents(){
		
		for(int r = 0; r < numOfResources; r++){
			for(int i = 0; i < agents.length; i++){
				
				successfulResourceUsageArray[i] = agents[i].getSuccessfulResourceUsage(r)/iterations;
			}
			
			Plot resourceUsagePlotAgent = new Plot("Resource Usage Percentage: MemorySize: " + memorySize + " | Number of Agents: " + numOfAgents + " | Number of Strategies: " + numOfStrategies + " | Iterations: " + iterations, agentsIndex, successfulResourceUsageArray);

		}
		
	}
	
	//--------------------------------------------------------------------------------------------------------- EMG L(p) and P(p) ----------------------------------------


	//Calculates L(p) and P(p) based on the information collected in a simulation run of T iterations. 
	public void calculateEMG_LandPFunction(){
		
		for(int i = 0; i < probUseArrSize; i++){
			
			double probUsage = probabilityUsageArray[i];//gets the usage count for each probability
			double switchRate = probabilitySwitchRateArray[i];//gets the count for how many times a probability was switched from being used to not used. 
			double lAnswer;// = probUsage / switchRate;
			
			double probLast10PercentUsage = frequencyDistArray[i];//gets the usage count for the last 10% of iterations
			double pAnswer;
			
			if(switchRate == 0){
				switchRate = 1;
			}
			
			//answer for Lifetime function, takes the count of how many times a probability is used and the sum is divided by the number of times the probability was switched from being used to not used
			lAnswer = probUsage / switchRate;
			
			//answer/average for P(p) function, takes the count for the last 10% of iterations and divides by 10% of the iterations
			pAnswer = probLast10PercentUsage / (iterations / 10);
			
			//keeps a running total to take a final average after 10 games
			lifeTimeArray[i] += lAnswer;
			
			// keeps the value i
			finalAverageProbabilityUseArray[i] += pAnswer;
		}
		
	}
	
	//Plots the L(p) function for the EMG
	public void plotLifeTimeEMG() {

		// testing
		System.out.println("THIS IS WHERE THE LIFETIME STARTS---------------------------------------------------------------------------------------------------------");
		
		for (int i = 0; i < probUseArrSize; i++) {

			double temp = lifeTimeArray[i];
			temp = temp / lifeTimeRuns;
			lifeTimeArray[i] = temp;

			// testing
			System.out.println(temp);

		}
			
		Plot probabilityLifetimePlot = new Plot("Probability Lifetime L(p): Threshold: " + threshold + " | Range: " + range + " | Number of Agents: " + numOfAgents + 
				" | Iterations: " + iterations + " | MemorySize: " + memorySize, probabilityIndexArray, lifeTimeArray);
		
	}
	
	
	//Plots the P(p) function for the EMG
	public void plotAverageProbabilityUse(){
		
		System.out.println("P FUNCTION STARTS---------------------------------------------------------------------------------------------------------");
		
		for(int i = 0; i < probUseArrSize; i++){

			finalAverageProbabilityUseArray[i] = (finalAverageProbabilityUseArray[i] / lifeTimeRuns);
			
			System.out.println(finalAverageProbabilityUseArray[i]);
			
		}
		
		Plot averageProbabilityUsePlot = new Plot("Agent Probability Use: Threshold: " + threshold + " | Range: " + range + " | Number of Agents: " + numOfAgents + 
				" | Iterations: " + iterations + " | MemorySize: " + memorySize, probabilityIndexArray, finalAverageProbabilityUseArray);
	}
	
	
	
	

}
