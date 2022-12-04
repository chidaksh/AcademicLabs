package processor.pipeline;

import processor.Processor;
import generic.Operand;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			int alu_result = EX_MA_Latch.getALU();
			// System.out.println("MA alu result: " + alu_result);
			MA_RW_Latch.setisWrite(false);
			MA_RW_Latch.setisEnd(false);
			MA_RW_Latch.setisLoad(false);
			if(EX_MA_Latch.getisWrite()){
				MA_RW_Latch.setAluResult(alu_result);
				// System.out.println("Entered iswriteMA");
				MA_RW_Latch.setisWrite(true);
				Operand rd = EX_MA_Latch.getDestinationOperand();
				MA_RW_Latch.setDestinationOperand(rd);
			}
			if(EX_MA_Latch.getisLoad()){
				int load_result = containingProcessor.getMainMemory().getWord(alu_result);
				MA_RW_Latch.setLoadResult(load_result);
				MA_RW_Latch.setisLoad(true);
			}
			if(EX_MA_Latch.getisStore()){
				int val_store = containingProcessor.getRegisterFile().getValue(
					EX_MA_Latch.getrs1().getValue());
				containingProcessor.getMainMemory().setWord(alu_result,val_store);
			}
			if(EX_MA_Latch.getisEnd()){
				MA_RW_Latch.setisEnd(true);
			}
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
