package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static long numberOfInstructions;
	static int numberOfOFStageInstructions;
	static long numberOfCycles;
	static int numberOfBranchTaken;
	static int numberOfRegisterWriteInstructions;
	static float IPC;
	

	public static void printStatistics(String statFile)	{
		try	{
			// TODO add code here to print statistics in the output file
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed: " + numberOfInstructions);
			writer.println("Number of cycles taken: " + numberOfCycles);
			writer.println("IPC: " + IPC);
			writer.close();
		}
		catch(Exception e) {
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(long numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(long numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static long getNumberOfInstructions() {
		return numberOfInstructions;
	}

	public static long getNumberOfCycles() {
		return numberOfCycles;
	}
	
	public static void setNumberOfOFInstructions(int numberOfOFStageInstructions) {
		Statistics.numberOfOFStageInstructions = numberOfOFStageInstructions;
	}
	
	public static int getNumberOfOFInstructions() {
		return numberOfOFStageInstructions;
	}
	
	public static void setNumberOfBranchTaken(int numberOfBranchTaken) {
		Statistics.numberOfBranchTaken = numberOfBranchTaken;
	}
	
	public static int getNumberOfBranchTaken() {
		return numberOfBranchTaken;
	}
	
	public static void setnumberOfRegisterWriteInstructions(int numberOfRegisterWriteInstructions) {
		Statistics.numberOfRegisterWriteInstructions = numberOfRegisterWriteInstructions;
	}
	
	public static int getNumberOfRegisterWriteInstructions() {
		return numberOfRegisterWriteInstructions;
	}
	
	public static void setIPC(float throughput) {
		Statistics.IPC = throughput ;
	}
}
