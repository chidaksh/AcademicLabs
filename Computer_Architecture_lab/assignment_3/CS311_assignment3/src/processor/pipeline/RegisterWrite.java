package processor.pipeline;

import processor.Processor;
import generic.Operand;
import generic.Simulator;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			if (MA_RW_Latch.getisEnd()){
				Simulator.setSimulationComplete(true);
			}
			
			if(MA_RW_Latch.getisWrite()){
				Operand rd = MA_RW_Latch.getDestinationOperand();
				int alu_result = MA_RW_Latch.getAluResult();
				// System.out.println("RW alu result: " + alu_result);
				if(MA_RW_Latch.getisLoad()){
					int load_result = MA_RW_Latch.getLoadResult();
					int target = rd.getValue();
					containingProcessor.getRegisterFile().setValue(target, load_result);
				}
				else{
					int target = rd.getValue();
					// System.out.println("target : " + target);
					containingProcessor.getRegisterFile().setValue(target, alu_result);
				}
			}
			MA_RW_Latch.setisWrite(false);
			MA_RW_Latch.setisEnd(false);
			MA_RW_Latch.setisLoad(false);
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
