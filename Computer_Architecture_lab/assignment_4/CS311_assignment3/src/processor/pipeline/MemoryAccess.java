package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA() {
		Instruction instruction = null;
		try {
			instruction = EX_MA_Latch.getInstruction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (instruction != null) {
			int alu_output = EX_MA_Latch.getAluResult();
			MA_RW_Latch.setAluOutput(alu_output);
			if (instruction != null) {
				OperationType op = instruction.getOperationType();
				if (op.toString().equals("store")) {
					int store_result = containingProcessor.getRegisterFile()
							.getValue(instruction.getSourceOperand1().getValue());
					containingProcessor.getMainMemory().setWord(alu_output, store_result);
				} else if (op.toString().equals("load")) {
					int res_ld = containingProcessor.getMainMemory().getWord(alu_output);
					MA_RW_Latch.setLoadOutput(res_ld);
				}
				MA_RW_Latch.setInstruction(instruction);
			}
		}
	}

}
