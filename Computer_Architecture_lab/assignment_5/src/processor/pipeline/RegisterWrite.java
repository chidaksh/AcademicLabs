package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performRW() {
		if (MA_RW_Latch.isRW_enable()) {
			if (!MA_RW_Latch.getIsNop()) {
				int aluResult = MA_RW_Latch.getAluResult();
				int rd = MA_RW_Latch.getDestinationOperand();
				String opcode = MA_RW_Latch.opcode;
				if (MA_RW_Latch.getIsLoad()) {
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					MA_RW_Latch.setIsLoad(false);
				} else {
					if (!opcode.equals("11101") && !opcode.equals("11000") && !opcode.equals("11001")
							&& !opcode.equals("11010") && !opcode.equals("11011") && !opcode.equals("11100")) {
						containingProcessor.getRegisterFile().setValue(rd, aluResult);
					}
				}

				MA_RW_Latch.setRW_enable(false);
				if (MA_RW_Latch.opcode.equals("11101")) {
					Simulator.setSimulationComplete(true);
					IF_EnableLatch.setIF_enable(false);
				}
			}
			// TODO

		} else {

		}
	}

}
