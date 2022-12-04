package processor.pipeline;

import processor.Processor;
import generic.Instruction.OperationType;
import generic.Operand;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performEX() {
		// TODO
		for (int i=0;i<32;i++){
			// System.out.println(i+ ": " + containingProcessor.getRegisterFile().getValue(i));
		}
		if (OF_EX_Latch.isEX_enable()) {
			String encoded_type = OF_EX_Latch.getEncodedType();
			int alu_result = 0;
			OperationType op = OF_EX_Latch.getoperation();
			EX_MA_Latch.setisWrite(false);
			EX_MA_Latch.setisLoad(false);
			EX_MA_Latch.setisStore(false);
			EX_MA_Latch.setisEnd(false);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			// System.out.println("2: "  + currentPC);
			if (encoded_type == "R3") {
				int op1 = containingProcessor.getRegisterFile().getValue(
						OF_EX_Latch.getrs1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
						OF_EX_Latch.getrs2().getValue());
				// System.out.println("operands: " + op1 + " " + op2);
				Operand rd = OF_EX_Latch.getrd();
				switch (op) {
					case add:
						alu_result = op1 + op2;
						break;
					case sub:
						alu_result = op1 - op2;
						break;
					case mul:
						alu_result = op1 * op2;
						break;
					case div:
						alu_result = op1 / op2;
						int remainder = op1 % op2;
						// System.out.println("remainder: "+remainder);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case and:
						alu_result = op1 & op2;
						break;
					case or:
						alu_result = op1 | op2;
						break;
					case xor:
						alu_result = op1 ^ op2;
						break;
					case slt:
						if (op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case sll:
						alu_result = op1 << op2;
						break;
					case srl:
						alu_result = op1 >>> op2;
						break;
					case sra:
						alu_result = op1 >> op2;
						break;
					default:
						break;
				}
				EX_MA_Latch.setisWrite(true);
				EX_MA_Latch.setALU(alu_result);
				EX_MA_Latch.setDestinationOperand(rd);
			} else if (encoded_type == "R2I_0") {
				int op1 = containingProcessor.getRegisterFile().getValue(
						OF_EX_Latch.getrs1().getValue());
				int op2 = OF_EX_Latch.getImmediate();
				Operand rd = OF_EX_Latch.getrd();

				switch (op) {
					case addi:
						alu_result = op1 + op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case subi:
						alu_result = op1 - op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case muli:
						alu_result = op1 * op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case divi:
						alu_result = op1 / op2;
						int remainder = op1 % op2;
						// System.out.println("remainder: "+remainder);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						EX_MA_Latch.setisWrite(true);
						break;
					case andi:
						alu_result = op1 & op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case ori:
						alu_result = op1 | op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case xori:
						alu_result = op1 ^ op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case slti:
						if (op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						EX_MA_Latch.setisWrite(true);
						break;
					case slli:
						alu_result = op1 << op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case srli:
						alu_result = op1 >>> op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case srai:
						alu_result = op1 >> op2;
						EX_MA_Latch.setisWrite(true);
						break;
					case load:
						alu_result = op1 + op2;
						EX_MA_Latch.setisLoad(true);
						EX_MA_Latch.setisWrite(true);
						break;
					case store:
						op1 = containingProcessor.getRegisterFile().getValue(
								OF_EX_Latch.getrd().getValue());
						Operand rs1 = OF_EX_Latch.getrs1();
						EX_MA_Latch.setrs1(rs1);
						alu_result = op1 + op2;
						EX_MA_Latch.setisStore(true);
						break;
					default:
						break;
				}
				EX_MA_Latch.setALU(alu_result);
				EX_MA_Latch.setDestinationOperand(rd);
			} else if (encoded_type == "R2I_1") {
				int op1 = containingProcessor.getRegisterFile().getValue(
						OF_EX_Latch.getrs1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
						OF_EX_Latch.getrd().getValue());
				int imm = OF_EX_Latch.getImmediate();
				Operand rd = OF_EX_Latch.getrd();
				EX_MA_Latch.setisWrite(false);
			    EX_MA_Latch.setisLoad(false);

				switch (op) {
					case beq:
						if (op1 == op2) {
							alu_result = imm + currentPC;
							EX_IF_Latch.setisBranchTaken(true);
							EX_IF_Latch.setProgramCounter(alu_result);
							// System.out.println("3 PC: "  + alu_result);
						}
						break;
					case bne:
						if (op1 != op2) {
							alu_result = imm + currentPC;
							EX_IF_Latch.setisBranchTaken(true);
							EX_IF_Latch.setProgramCounter(alu_result);
							// System.out.println("4 PC: "  + alu_result);
						}

						break;
					case blt:
						if (op1 < op2) {
							alu_result = imm + currentPC;
							EX_IF_Latch.setisBranchTaken(true);
							EX_IF_Latch.setProgramCounter(alu_result);
							// System.out.println("5 PC: "  + alu_result);
						}
						break;
					case bgt:
						if (op1 > op2) {
							alu_result = imm + currentPC;
							EX_IF_Latch.setisBranchTaken(true);
							EX_IF_Latch.setProgramCounter(alu_result);
							// System.out.println("6 PC: "  + alu_result);
						}
						break;
					default:
						break;
				}
				EX_MA_Latch.setDestinationOperand(rd);
			} else if (encoded_type == "RI_0") {
				Operand rd = OF_EX_Latch.getrd();
				EX_IF_Latch.setisBranchTaken(true);
				if (OF_EX_Latch.getisImmediate()) {
					int imm = OF_EX_Latch.getImmediate();
					alu_result = (currentPC) + imm;
					EX_IF_Latch.setProgramCounter(alu_result);
					// System.out.println("7 PC: "  + alu_result);
				} else {
					int offset = containingProcessor.getRegisterFile().getValue(
							OF_EX_Latch.getrd().getValue());
					alu_result = (currentPC) + offset;
					EX_IF_Latch.setProgramCounter(alu_result);
					// System.out.println("8 PC: "  + alu_result);
				}
				EX_MA_Latch.setDestinationOperand(rd);
			} else {
				EX_MA_Latch.setisEnd(true);
			}
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
		}
	}
}
