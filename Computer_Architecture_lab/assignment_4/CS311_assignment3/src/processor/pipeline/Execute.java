package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performEX() {
		Instruction inst = null;
		try {
			inst = OF_EX_Latch.getInstruction();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (inst != null) {
			EX_MA_Latch.setInstruction(inst);
			OperationType op = inst.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(op);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;

			int alu_result = 0;

			if (opcode < 21 && opcode % 2 == 0) {
				int op1 = containingProcessor.getRegisterFile().getValue(
						inst.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
						inst.getSourceOperand2().getValue());
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
						System.out.println("remainder: " + remainder);
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
			} else if (opcode < 23) {
				int con = inst.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(con);
				int op2 = inst.getSourceOperand2().getValue();
				switch (op) {
					case addi:
						alu_result = op1 + op2;
						break;
					case subi:
						alu_result = op1 - op2;
						break;
					case muli:
						alu_result = op1 * op2;
						break;
					case divi:
						alu_result = op1 / op2;
						int remainder = op1 % op2;
						// System.out.println("remainder: "+remainder);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case andi:
						alu_result = op1 & op2;
						break;
					case ori:
						alu_result = op1 | op2;
						break;
					case xori:
						alu_result = op1 ^ op2;
						break;
					case slti:
						if (op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case slli:
						alu_result = op1 << op2;
						break;
					case srli:
						alu_result = op1 >>> op2;
						break;
					case srai:
						alu_result = op1 >> op2;
						break;
					case load:
						alu_result = op1 + op2;
						break;
					default:
						break;
				}
			} else if (opcode == 23) {
				int op1 = containingProcessor.getRegisterFile().getValue(
						inst.getDestinationOperand().getValue());
				int op2 = inst.getSourceOperand2().getValue();
				alu_result = op1 + op2;
			} else if (opcode == 24) {
				// System.out.println("jump instruction");
			} else if (opcode < 29) {
				int op1 = containingProcessor.getRegisterFile().getValue(
						inst.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(inst.getSourceOperand2().getValue());
				int imm = inst.getDestinationOperand().getValue();
				switch (op) {
					case beq:
						if (op1 == op2) {
							alu_result = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(alu_result);
							EX_MA_Latch.setFlag(1);
							// System.out.println("3 PC: " + alu_result);
						}
						break;
					case bne:
						if (op1 != op2) {
							alu_result = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(alu_result);
							EX_MA_Latch.setFlag(1);
							// System.out.println("4 PC: " + alu_result);
						}
						break;
					case blt:
						if (op1 < op2) {
							alu_result = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(alu_result);
							EX_MA_Latch.setFlag(1);
							// System.out.println("5 PC: " + alu_result);
						}
						break;
					case bgt:
						System.out.println("Entered bgt " + op1 + " " + op2);
						if (op1 > op2) {
							alu_result = imm + currentPC;
							containingProcessor.getRegisterFile().setProgramCounter(alu_result);
							EX_MA_Latch.setFlag(1);
							// System.out.println("6 PC: " + alu_result);
						}
						break;
					default:
						break;
				}

			} else if (op.toString().equals("nop")) {
				// System.out.print("nop instruction");
			}
			EX_MA_Latch.setAluResult(alu_result);
		}
	}
}
