package processor.pipeline;

import processor.Processor;
public class InstructionFetch {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF() {
		if (IF_EnableLatch.isIF_enable()) {
			if (EX_IF_Latch.getisBranchTaken()) {
				int newPC = EX_IF_Latch.getProgramCounter();
				containingProcessor.getRegisterFile().setProgramCounter(newPC);
				// System.out.println("9 PC: " + newPC);
				EX_IF_Latch.setisBranchTaken(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			// System.out.println("1 " + currentPC);
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
