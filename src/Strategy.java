import java.util.Random;


public class Strategy {

	private double[] scores;//scores used to rank strategies for different resources
	private double score;//score used to rank strategies
	private int[] values;//Stores the strategy decision values
	private int numOfDecisions;//range of random number of decisions
	private int memorySize;//This value is used to generate the number of possible strategies according to this equation 2^2^memorySize
	
	public Strategy(int memorySize, int numOfResources){
		
		this.memorySize = memorySize;
		numOfDecisions = 2; 
		values = new int[(int) Math.pow(2, memorySize)];//creates an array of strategies 2^m large
		randomInitialize();
		initializeScoreArray(numOfResources);
	}
	
	/*Creates a strategy's array of values with random bits based on the number of decisions.*/
	public void randomInitialize(){
		
		for (int i = 0; i < values.length; i++){
			
			values[i] = randomInt(numOfDecisions);
			
		}
	}
	
	/*Returns a random number from 0 to n-1 to fill a strategies array of values*/
	public int randomInt(int numOfDecisions){
		
		Random rnd = new Random();
		int random1 = rnd.nextInt(numOfDecisions);
		return random1;
	}
	
	//This array holds the different scores each resource has for a particular strategy. 
	//For example this strategy may work great for resource 1 and terrible for resource 2.
	//This keeps track of the appropriate scores. 
	public void initializeScoreArray(int numOfResources) {

		scores = new double[numOfResources];
		
		for(int i = 0; i < numOfResources; i++){
			scores[i] = 0;
		}
		
	}
	
	//Returns the value associated with the index of the strategy.
	public int getValue(int i){
		
		return values[i];
		
	}
	
	/*Updates the score of the strategy based on its value compared to the winning value*/
	public void setScore(double d){
		
		score = d;
		
	}
	
	//Returns the score of a strategy
	public double getScore(){
		
		return score;
		
	}
	
	/*Updates the score of the strategy based on its value compared to the winning value and stores it for the appropriate resource*/
	public void setScore(double d, int resourceIndex){
		
		scores[resourceIndex] = d;
		
	}
	
	//returns the score of a strategy
	public double getScore(int resourceIndex){
		
		return scores[resourceIndex];
		
	}
	
	
//	public static void main(String[] args) {
//		
//		Strategy s = new Strategy();
//	}
}
