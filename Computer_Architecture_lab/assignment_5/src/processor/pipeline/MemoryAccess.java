package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.*;

public class MemoryAccess implements Element {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	Cache l1dcache;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch,
			MA_RW_LatchType mA_RW_Latch, Cache cache) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.l1dcache = cache;
	}

	public void performMA() {
		// if(EX_MA_Latch.MA_enable == false) MA_RW_Latch.RW_enable = false;
		if (EX_MA_Latch.isMA_enable() && EX_MA_Latch.isBusy == false) {
			if (EX_MA_Latch.getIsNop()) {
				MA_RW_Latch.setIsNop(true);
				MA_RW_Latch.setDestinationOperand(75000);
			} else {
				MA_RW_Latch.setIsNop(false);
				int aluResult = EX_MA_Latch.getAluResult();
				int rs1 = EX_MA_Latch.getOperand1();
				int rs2 = EX_MA_Latch.getOperand2();
				int rd = EX_MA_Latch.getDestinationOperand();
				int imm = EX_MA_Latch.getImm();
				String opcode = EX_MA_Latch.getOpcode();

				MA_RW_Latch.setPC(EX_MA_Latch.getPC());

				MA_RW_Latch.setAluResult(aluResult);
				MA_RW_Latch.setOperand1(rs1);
				MA_RW_Latch.setOperand2(rs2);
				MA_RW_Latch.setDestinationOperand(rd);
				MA_RW_Latch.setImm(imm);
				MA_RW_Latch.setOpcode(opcode);

				if (opcode.equals("10110")) { // load
					MA_RW_Latch.setIsLoad(true);
					EX_MA_Latch.setisBusy(true);
					Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
									Clock.getCurrentTime() + this.l1dcache.latency,
									this,
									this.l1dcache, aluResult));
					EX_MA_Latch.setMA_enable(false);
					return;
				}
				if (opcode.equals("10111")) { // store
					EX_MA_Latch.setisBusy(true);
					Simulator.getEventQueue().addEvent(
							new MemoryWriteEvent(
									Clock.getCurrentTime() + this.l1dcache.latency,
									this,
									this.l1dcache,
									aluResult,
									rs1));
					EX_MA_Latch.setMA_enable(false);
					return;

				}
			}
			EX_MA_Latch.setMA_enable(false);
			if (EX_MA_Latch.opcode.equals("11101") == true) {
				EX_MA_Latch.setMA_enable(false);
			}
			MA_RW_Latch.setRW_enable(true);
		}
		// TODO
	}

	@Override
	public void handleEvent(Event e) {
		if (e.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			MA_RW_Latch.setAluResult(event.getValue());
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setisBusy(false);
		} else {
			EX_MA_Latch.setisBusy(false);
		}

	}

}
