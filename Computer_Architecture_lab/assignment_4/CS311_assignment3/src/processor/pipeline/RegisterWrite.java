package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType ifOfLatchType) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = ifOfLatchType;
	}

	public void performRW() {
		Instruction inst = null;
		try {
			inst = MA_RW_Latch.getInstruction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (inst != null) {

			int alu_output = MA_RW_Latch.getAluOutput();
			OperationType op = inst.getOperationType();

			switch (op) {
				case store:
				case jmp:
				case beq:
				case bne:
				case blt:
				case bgt:
				case nop:
					break;
				case load:
					int load_output = MA_RW_Latch.getLoadOutput();
					int DestinationOperand = inst.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(DestinationOperand, load_output);
					break;
				case end:
					containingProcessor.getRegisterFile()
							.setProgramCounter(containingProcessor.getRegisterFile().getFreezedprogramCounter());
					Simulator.setSimulationComplete(true);
					break;
				default:
					DestinationOperand = inst.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(DestinationOperand, alu_output);
					break;
			}
		}
	}
}
