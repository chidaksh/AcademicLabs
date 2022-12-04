package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		simulationComplete = false;
	}

	static void loadProgram(String assemblyProgramFile)
	{
		//
		// TODO
		// 1. load the program into memory according to the program layout described
		//    in the ISA specification
		try (
				InputStream inputStream = new FileInputStream(assemblyProgramFile);
		) {
			MainMemory MainMemory =new MainMemory();
			RegisterFile RegisterFile =new RegisterFile();
			byte[] buffer = new byte[4];
			int i = 0;
			int j = 0;
			int pc = 0;
			while (inputStream.read(buffer) != -1) {
				ByteBuffer wrapped = ByteBuffer.wrap(buffer);
				int num = wrapped.getInt();
				//System.out.println(num);
				if(j==0){
					pc = num;
				}
				if(j!=0){
					MainMemory.setWord(i,num);
					i+=1;
				}
				j+=1;

			}
			processor.setMainMemory(MainMemory);

			// 2. set PC to the address of the first instruction in the main
			RegisterFile.setProgramCounter(pc);

			// 3. set the following registers:
			//     x0 = 0
			//     x1 = 65535
			//     x2 = 65535
			RegisterFile.setValue(0,0);
			RegisterFile.setValue(1,65535);
			RegisterFile.setValue(2,65535);

			processor.setRegisterFile(RegisterFile);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//System.out.println(processor.getMainMemory().getContentsAsString(0,4));
		//System.out.println(processor.getRegisterFile().getContentsAsString());
	}

	public static void simulate()
	{
		while(!simulationComplete)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}

		// TODO
		// set statistics

	}

	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
