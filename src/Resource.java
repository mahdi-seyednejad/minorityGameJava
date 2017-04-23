public class Resource {

	private double capacity;//Each resource has an assigned capacity value
	private int memorySize;//This determines the length of the memory array
	private int[] memory;//Each resource keeps track of its own memory. Memory is used to decide what index an agents should select from their strategy
	
	public Resource(double capacity, int memorySize) {

		this.capacity = capacity;
		this.memorySize = memorySize;
		intializeMemory();
		
	}
	
	//---------------------------------------------------------------------------------------------------------Resource Memory Methods----------------------------------------
	
	//Creates a resource's memory array
	public void intializeMemory(){
		
		memory = new int[memorySize];
		
		for(int i = 0; i < memorySize; i++){
			
			memory[i] = 0;
			
		}
	}
	
	//sets the resources memorySize
//	public void setResourceMemorySize(int memSize){
//		
//		
//	}
	
	//Sets the resource's memory array to the next memory array based on the last winning choice
	public void setBinaryMemory(int [] mem){
		
		for(int i = 0; i < mem.length; i++){
			
			memory[i] = mem[i];
		}
	}
	
	//Returns a resource's memory array. It is used to extract the decimal value in the game to choose the index of an agent's strategy
	public int[] getBinaryMemory(){
		
		return memory;
		
	}
	
	/*Updates the current memory with the last winning bit*/
	public void updateMemory(int currentOutcome){
		
		for(int i = 0; i < memory.length - 1; i++){
			memory[i] = memory[i + 1];
		}
		memory[memory.length - 1] = currentOutcome;
		
	}
	
	
	//---------------------------------------------------------------------------------------------------------Resource Capacity Methods----------------------------------------
	
	
//	public void setResourceCapacity(double capacity){
//		
//		
//	}

	//Returns a resource's capacity value
	public double getCapacity(){
		
		return capacity;
		
	}
	
	
}
