package processor.pipeline;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.*;

public class InstructionFetch implements Element {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	Cache l1icache;
	int currentPC;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, Cache cache) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.l1icache = cache;
	}

	public void performIF() {
		if (IF_EnableLatch.isIF_enable()) {
			if (IF_EnableLatch.getisBusy()) {
				return;
			}
			Simulator.noOfInstructions++;
			this.currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			if (EX_IF_Latch.getIsBranchTaken()) {
				currentPC = currentPC + EX_IF_Latch.offset - 1;
				EX_IF_Latch.setIsBranchTaken(false);
			}
			Simulator.getEventQueue().addEvent(
					new MemoryReadEvent(Clock.getCurrentTime() + this.l1icache.latency,
							this, this.l1icache, currentPC));
			IF_EnableLatch.setisBusy(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
		if (IF_OF_Latch.getisBusy()) {
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		} else {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			if (!EX_IF_Latch.getIsBranchTaken()) {
				IF_OF_Latch.setInstruction(event.getValue());
			} else
				IF_OF_Latch.setInstruction(0);
			IF_OF_Latch.setPC(this.currentPC);
			containingProcessor.getRegisterFile().setProgramCounter(this.currentPC + 1);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setisBusy(false);
		}
	}

}
