import java.util.Arrays;
import java.util.Random;


public class Agent {
	//agent class holds strategies
	//pass in strategy through agent
	
	private double score;
	private int winCount;
	private int subWinCount;
	private int decision;
	private int[] decisions;
	private int strategyIndex;
	private Strategy[] strategies;
	private int numberOfStrategies;
	private int numOfResources;
	private double[] successfulResourceUsage;
	private double probability;
	private double oldProbability;
	private double randomDouble;
	//private int[] probabilityUseArray;
	//private int totalStrategies;
	
	//Regular Minority Game. Agent passes in the number of strategies we want to make based on the memorySize, making the array 2^numOfStrategies
	public Agent(int numOfStrategies, int memorySize, int numOfResources){
		
		this.numberOfStrategies = numOfStrategies;
		this.numOfResources = numOfResources;
		//totalStrategies = (int)Math.pow(2, numberOfStrategies);
		strategies = new Strategy[numberOfStrategies];
		strategyIndex = 0;
		setScore(0);
		initializeStrategies(memorySize, numOfResources);
		intializeResourceDecisions();
		initializeResourceUsageArray();
		winCount = 0;
	}
	
	//Evolutionary Minority Game Constructor where strategies are not needed. Each agent is assigned a random probability to use on a global strategy in the game.
	public Agent(int numOfResources, int probArrSize){
		
		//randomly select probability
		probability = generateRandomDouble();
		this.numOfResources = numOfResources;
		setScore(0);
		intializeResourceDecisions();
		initializeResourceUsageArray();
		//probabilityUseArray = new int[probArrSize];
		//initializeProbabilityUseArray(probArrSize);
		winCount = 0;
	}

	//This creates an agent's strategies based on the memorySize. The number of resources is needed to keep track of a strategy's score for different resources
	public void initializeStrategies( int memorySize, int numOfResources){
		
		for(int i = 0; i < strategies.length; i++){
			strategies[i] = new Strategy(memorySize, numOfResources);
		}
	}
	
	// do not need anymore
//	public void initializeProbabilityUseArray(int probArrSize){
//		
//		for(int i = 0; i < probArrSize; i++){
//			
//			probabilityUseArray[i] = 0;
//			
//		}
//		
//		
//	}
	
	//This fills the strategies agents' can choose from
//	public void populateStrategies(){
//		
//		for(int i = 0; i < strategy.length; i++){
//			strategy[i].randomInitialize();
//		}
//	}
	
	//returns the strategy index with the best score
	
	
	//returns the index of the strategy with the highest score
//	public int getStrategyIndex()
//	{
//		strategyIndex = 0;
//		double highScore = strategies[0].getScore();
//		
//		for(int i = 0; i < strategies.length; i++){
//			if(strategies[i].getScore() > highScore){
//				highScore = strategies[i].getScore();
//				this.strategyIndex = i;
//			}
//		}
//		
//		return strategyIndex;
//	}
	
	//---------------------------------------------------------------------------------------------------------Agent Decision Making Methods----------------------------------------
	
	//Used for computing different scores for different strategies for different resources
	public int getStrategyIndex(int resource) {
		strategyIndex = 0;
		double highScore = strategies[0].getScore(resource);

		for (int i = 0; i < strategies.length; i++) {

			if (strategies[i].getScore(resource) > highScore) {

				highScore = strategies[i].getScore(resource);
				this.strategyIndex = i;

			}
		}
		return strategyIndex;
	}
	
	//Used for computing the score of a strategy used on all resources
	public int getStrategyIndex() {
		strategyIndex = 0;
		double highScore = strategies[0].getScore();

		for (int i = 0; i < strategies.length; i++) {

			if (strategies[i].getScore() > highScore) {

				highScore = strategies[i].getScore();
				this.strategyIndex = i;
			}
		}
		return strategyIndex;
	}
	
	//This array is used to store the decisions an agent makes for each resource
	public void intializeResourceDecisions(){
		
		decisions = new int[numOfResources];
		
		for(int i = 0; i < numOfResources; i++){
			
			decisions[i] = 0;
		}
	}
	
	//Decides if the strategy value matches the memory value, classic Minority Game with the option of using different strategies for different resources.
	public int makeDecision(int currentMemoryValue, int resource, boolean separateStrategyScoring){
		
		if(separateStrategyScoring)
			decision = getStrategyValue(getStrategyIndex(resource), currentMemoryValue);
		else
			decision = getStrategyValue(getStrategyIndex(), currentMemoryValue);
			
		if(decision == 1){
			
			setDecision(1, resource);
			//System.out.print(" Decision: " + decision + " | Strategy index: " + strategyIndex);
			//System.out.println();
			return 1;
			
		}
		else{
			
			setDecision(0, resource);
			//System.out.print("Decision: " + decision + " | Strategy index: " + strategyIndex);// + " | Strategy Score: " + strategies[strategyIndex].getScore() + " | Player Score: " + getScore() + " | Player win count: " + getWinCount());
			//System.out.println();
			return 0;
		}	
	}

	//Print statement for testing
	public void printValues(){
		
		System.out.print(" Decision: " + decision + " | Strategy index: " + strategyIndex);
		
	}
	
	//Sets the decision of the current iteration for current resource.
	public void setDecision(int dec, int resourceIndex){
		
		decision = dec;
		decisions[resourceIndex] = decision;
		
	}

	//Returns an agent's current decision
	public int getDecision() {

		return decision;
		
	}
	
	//Returns the current decision at the specified resource index.
	public int getDecision(int resourceIndex){
		
		return decisions[resourceIndex];
		
	}
	
	//Returns all of the agent's decisions based on how many resources decisions were made on. 
	public int[] getDecisions() {

		return decisions;
		
	}
	
	//Get the value from the specific index based on the current memory from the current strategy
	public int getStrategyValue(int indexOfStrategy, int indexOfDecision){
		
		
		return strategies[indexOfStrategy].getValue(indexOfDecision);
		
	}
	
	
	//---------------------------------------------------------------------------------------------------------Agent Scoring methods----------------------------------------
	
	
	//Updates the score of a player based on the outcome of their decision and the global outcome
	public void setScore(double d){
		
		score = d;
		
	}
	
	//returns the agents current score
	public double getScore(){
		
		return score;
		
	}
	
	
	//---------------------------------------------------------------------------------------------------------Strategy Scoring Methods----------------------------------------
	
	
	//sets the current strategy score
	public void setStrategyScore(double d){
		
		strategies[strategyIndex].setScore(d);
		
	}
	
	//Sets the current strategy score for multiple resources using different scores for different strategies
	public void setStrategyScore(double d, int resourceIndex){
		
		strategies[strategyIndex].setScore(d, resourceIndex);
		
	}
	
	//Used for returning the score of the same strategy used on all resources
	public double getStrategyScore(){
		
		return strategies[strategyIndex].getScore();
		
	}
	
	//Used for returning the score for a specific resource index when evaluating different scores for different strategies for different resources
	//returns the current strategy score
	public double getStrategyScore(int resourceIndex){
		
		return strategies[strategyIndex].getScore(resourceIndex);
		
	}
	
	
	//---------------------------------------------------------------------------------------------------------Win Count Methods----------------------------------------
	
	
	//A simple winCount for one resource, mainly for testing simple scenarios
	public void simpleWinCount(int currentOutcome){
		
		if(currentOutcome == getDecision()){
			
			winCount++;
			
		}		
	}
	
	//Updates the win count for a player if their decision was the minority
	public void incrementMultipleResourcesWinCount(int[] outcomes){
		
		if(Arrays.equals(outcomes, decisions)){
			
			winCount++;
		}
	}
	
	//This method is used for incrementing the individual wins of the resources to ultimately see if the agent won all resources or a certain number of resources
	public void incrementWinCount(int outcome, int decisionIndex){
		
		if(outcome == decisions[decisionIndex]){
			
			subWinCount++;
		}
	}
	
//	public void setWinCount(int i){
//		winCount = i;
//		
//	}
	
	//Returns an agents win count
	public int getWinCount(){
		
		return winCount;
		
	}
	
	//Determines if an agent won with the needed amount of resources/subwins 
	public void finalWinCount(int subWins){
		
		if(subWinCount >= subWins){
			
			winCount++;
		}
		//reset sub win count at the end of each iteration
		subWinCount = 0;
		
	}
	
	//Divides an agents win count by the number of iterations to calculate the winning percentage
	public double calculateWinRate(double numOfIterations){
		
		double rate = getWinCount()/numOfIterations;
		return rate;
	}
	
	
	//---------------------------------------------------------------------------------------------------------Agent Resource Usage----------------------------------------
	
	
	//This array is used to hold an agents successful usage of a resource.
	public void initializeResourceUsageArray(){
		
		successfulResourceUsage = new double[numOfResources];
		
		for(int i = 0; i < numOfResources; i++){
			
			successfulResourceUsage[i] = 0;
		}
	}
	
	//For each iteration if an agent used the resource successfully than the value for that resource will be incremented
	public void incrementSuccessfulResourceUsage(int resourceIndex){ 
		
		successfulResourceUsage[resourceIndex] += 1;
		
	}
	
	//Returns an agents resource usage for the specified resource 
	public double getSuccessfulResourceUsage(int resourceIndex){
		
		return successfulResourceUsage[resourceIndex];
		
	}
	
	
	//---------------------------------------------------------------------------------------------------------Evolutionary Minority Game Specific Methods----------------------------------------
	
	
	
	//Evolutionary game make decision method based on probability.
	//If the random double generated is less than an agents probability value than they choose the global strategy choice, if not, the opposite is chosen.
	public int makeDecision(int globalChoice, int resource) {

		if (generateRandomDouble() <= probability) {
			setDecision(globalChoice, resource);
			return globalChoice;
		} else {
			if (globalChoice == 1){
				setDecision(0, resource);
				return 0;
			}
			else{
				setDecision(1, resource);
				return 1;
			}
		}
	}
	
	//Generates a random double used for the probability or when changing the probability within the range
	public double generateRandomDouble(){
		
		Random rnd = new Random();
		double random1 = rnd.nextDouble();
		randomDouble = random1;
		return random1;
		
	}
	
	//This checks the threshold of an agent in order to change agent's probability value. 
	//If their score drops below the threshold, their new probability will be chosen according to the range value declared at the beginning of the game
	//Finally their score is reset to zero
	public void checkThreshold(int threshold, double range){
		
		oldProbability = probability;
		
		if(score < threshold){
			
			double low = probability - range/2;
			double high = probability + range/2;
			double rand = generateRandomDouble(); 
			
			if(low < 0){
				low = 0;
				//System.out.println("Min scenario");
			}
			if(high > 1){
				high = 1;
				//System.out.println("Max scenario");
			}
			
			score = 0;
			double newRange = high - low;
			probability = (rand * newRange) + low;

//			System.out.println("Rand for threshold: " + rand);
//			System.out.println("Changed Probability from:" + oldProbability + " to " + probability);
		}
		
	}
	
	
	public double getRandomDoubleStoredValue(){
		return randomDouble;
		
	}
	
	//Returns an agents probability value
	public double getProbability(){
		return probability;
	}
	
	//Returns the previous probability value in order to compute the lifetime of probabilities
	public double getOldProbability(){
		
		return oldProbability;
		
	}
//	
	
	
// do not need anymore
//	public int getProbabilityUseValue(int index){
//		
//		return probabilityUseArray[index];
//		
//	}
//	
//	public void setProbabilityUseValue(int index){
//		
//		probabilityUseArray[index] = 1;
//		
//	}
//	
//	
	
//	public static void main(String[] args) {
//	
//		Agent a = new Agent(3);
//		a.getBestStrategyIndex();
//
//	}
}
