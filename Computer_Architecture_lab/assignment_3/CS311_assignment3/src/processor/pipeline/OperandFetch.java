package processor.pipeline;

import processor.Processor;
import java.util.*;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public static List<Instruction.OperationType> R3 = new ArrayList<Instruction.OperationType>() {
		{
			add(Instruction.OperationType.add);
			add(Instruction.OperationType.sub);
			add(Instruction.OperationType.mul);
			add(Instruction.OperationType.div);
			add(Instruction.OperationType.and);
			add(Instruction.OperationType.or);
			add(Instruction.OperationType.xor);
			add(Instruction.OperationType.slt);
			add(Instruction.OperationType.sll);
			add(Instruction.OperationType.srl);
			add(Instruction.OperationType.sra);
		}
	};
	public static List<Instruction.OperationType> R2I_0 = new ArrayList<Instruction.OperationType>() {
		{
			add(Instruction.OperationType.addi);
			add(Instruction.OperationType.subi);
			add(Instruction.OperationType.muli);
			add(Instruction.OperationType.divi);
			add(Instruction.OperationType.andi);
			add(Instruction.OperationType.ori);
			add(Instruction.OperationType.xori);
			add(Instruction.OperationType.slti);
			add(Instruction.OperationType.slli);
			add(Instruction.OperationType.srli);
			add(Instruction.OperationType.srai);
			add(Instruction.OperationType.load);
			add(Instruction.OperationType.store);
		}
	};
	public static List<Instruction.OperationType> R2I_1 = new ArrayList<Instruction.OperationType>() {
		{
			add(Instruction.OperationType.beq);
			add(Instruction.OperationType.bne);
			add(Instruction.OperationType.blt);
			add(Instruction.OperationType.bgt);
		}
	};

	public static String addressformat(Instruction.OperationType operation) {
		if (operation == Instruction.OperationType.jmp) {
			return "RI_0";
		} else if (operation == Instruction.OperationType.end) {
			return "RI_1";
		} else if (R3.contains(operation)) {
			return "R3";
		} else if (R2I_0.contains(operation)) {
			return "R2I_0";
		} else if (R2I_1.contains(operation))
			return "R2I_1";
		return "Error";
	}

	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public static String twosComplement(String bin) {
		String twos = "", ones = "";
		for (int i = 0; i < bin.length(); i++) {
			ones += flip(bin.charAt(i));
		}

		StringBuilder builder = new StringBuilder(ones);
		boolean b = false;
		for (int i = ones.length() - 1; i > 0; i--) {
			if (ones.charAt(i) == '1') {
				builder.setCharAt(i, '0');
			} else {
				builder.setCharAt(i, '1');
				b = true;
				break;
			}
		}
		if (!b) {
			builder.append("1", 0, 7);
		}
		twos = builder.toString();
		return twos;
	}

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			// TODO
			OperationType[] operationType = OperationType.values();
			String inst = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while (inst.length() != 32) {
				inst = "0" + inst;
			}
			String opcode = inst.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];
			String encoded_type = addressformat(operation);
			if (encoded_type == "R3") {

				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(inst.substring(5, 10), 2);
				rs1.setValue(registerNo);
				// System.out.println("register1 no: "+ registerNo);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(inst.substring(10, 15), 2);
				rs2.setValue(registerNo);
				// System.out.println("register2 no: "+ registerNo);

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(inst.substring(15, 20), 2);
				rd.setValue(registerNo);
				// System.out.println("register3 no: "+ registerNo);

				OF_EX_Latch.setrs1(rs1);
				OF_EX_Latch.setrs2(rs2);
				OF_EX_Latch.setrd(rd);
				OF_EX_Latch.setoperation(operation);
				OF_EX_Latch.setEncodedType(encoded_type);
			}

			else if (encoded_type == "R2I_0" || encoded_type == "R2I_1") {
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(inst.substring(5, 10), 2);
				rs1.setValue(registerNo);

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(inst.substring(10, 15), 2);
				rd.setValue(registerNo);

				String imm = inst.substring(15, 32);
				int immediate = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					immediate = (Integer.parseInt(imm, 2)) * (-1);
				}
				OF_EX_Latch.setrs1(rs1);
				OF_EX_Latch.setrd(rd);
				OF_EX_Latch.setImmediate(immediate);
				OF_EX_Latch.setoperation(operation);
				OF_EX_Latch.setEncodedType(encoded_type);
			}

			else if (encoded_type == "RI_0") {
				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				String imm = inst.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = (Integer.parseInt(imm, 2)) * (-1);
				}

				if (imm_val != 0) {
					OF_EX_Latch.setImmediate(imm_val);
					OF_EX_Latch.setisImmediatetrue();
				} else {
					int registerNo = Integer.parseInt(inst.substring(5, 10), 2);
					rd.setValue(registerNo);
					OF_EX_Latch.setrd(rd);
					OF_EX_Latch.setisImmediatefalse();
				}
				// System.out.println("immediate value: " + imm_val);
				// System.out.println("Register jump value: " + rd.getLabelValue());
				OF_EX_Latch.setoperation(operation);
				OF_EX_Latch.setEncodedType(encoded_type);
			} else {
				OF_EX_Latch.setoperation(operation);
				OF_EX_Latch.setEncodedType(encoded_type);
			}
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
