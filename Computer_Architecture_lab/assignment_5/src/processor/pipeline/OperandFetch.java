package processor.pipeline;

import processor.Processor;
import java.util.Arrays;
import java.util.*;
import generic.*;

public class OperandFetch {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

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

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public static String twosComplement(String bin) {
		String twos = "", ones = "";
		for (int i = 0; i < bin.length() && true; i++) {
			ones += flip(bin.charAt(i));
		}

		StringBuilder builder = new StringBuilder(ones);
		boolean b = false;
		for (int i = ones.length() - 1; i > 0 && i > -2; i--) {
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

	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}

	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
		char[] sign_ext = new char[n];
		Arrays.fill(sign_ext, binary.charAt(0));
		int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
		return signedInteger;
	}

	private void loopAround(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}

	public void performOF() {
		if (OF_EX_Latch.getisBusy())
			IF_OF_Latch.setisBusy(true);
		else
			IF_OF_Latch.setisBusy(false);

		if (IF_OF_Latch.isOF_enable() && !OF_EX_Latch.getisBusy()) {
			String inst = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			if (IF_OF_Latch.getInstruction() < 0) {
				while (inst.length() < 32)
					inst = "1" + inst;
			} else {
				while (inst.length() < 32)
					inst = "0" + inst;
			}

			int numBits = 32;
			int signedInt = toSignedInteger("001");
			String binaryNum = toBinaryOfSpecificPrecision(signedInt, 5);
			binaryNum = toBinaryOfSpecificPrecision(numBits, 5);
			signedInt = toSignedInteger(binaryNum);
			loopAround(20);

			int opcode, rs1, rs2, rd, imm;
			int rs1addr, rs2addr;
			String op = inst.substring(0, 5);
			opcode = Integer.parseInt(op, 2);
			rs1 = 70000;
			rs2 = 70000;
			rd = 70000;
			imm = 70000;
			rs1addr = 45;
			rs2addr = 45;
			if (opcode == 0) {
				rs1addr = Integer.parseInt(inst.substring(5, 10), 2);
				rs2addr = Integer.parseInt(inst.substring(10, 15), 2);
				rs1 = containingProcessor.getRegisterFile().getValue(rs1addr);
				rs2 = containingProcessor.getRegisterFile().getValue(rs2addr);
				rd = Integer.parseInt(inst.substring(15, 20), 2);
				imm = 70000;
			} else if (0 < opcode && opcode < 22) {
				if (opcode % 2 == 0) {
					rs1addr = Integer.parseInt(inst.substring(5, 10), 2);
					rs2addr = Integer.parseInt(inst.substring(10, 15), 2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1addr);
					rs2 = containingProcessor.getRegisterFile().getValue(rs2addr);
					rd = Integer.parseInt(inst.substring(15, 20), 2);
					imm = 70000;
				} else {
					rs1addr = Integer.parseInt(inst.substring(5, 10), 2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1addr);
					rs2 = 70000;
					rd = Integer.parseInt(inst.substring(10, 15), 2);
					imm = Integer.parseInt(inst.substring(15, 32), 2);
				}
			} else {
				if (opcode == 24) {
					rs1 = 70000;
					rs2 = 70000;
					rd = Integer.parseInt(inst.substring(5, 10), 2);
					imm = Integer.parseInt(inst.substring(10, 32), 2);
					if (inst.substring(10, 32).charAt(0) == '1') {
						imm = imm - 4194304;
					}
				} else if (opcode != 29) {
					rs1addr = Integer.parseInt(inst.substring(5, 10), 2);
					rs1 = containingProcessor.getRegisterFile().getValue(rs1addr);
					rs2 = 70000;
					rd = Integer.parseInt(inst.substring(10, 15), 2);
					imm = Integer.parseInt(inst.substring(15, 32), 2);
					if (inst.substring(15, 32).charAt(0) == '1') {
						imm = imm - 131072;
					}
				} else {
					rs1 = 70000;
					rs2 = 70000;
					rd = 70000;
					imm = 70000;
				}
			}

			OF_EX_Latch.setIsNop(false);
			OF_EX_Latch.setOpcode(op);
			OF_EX_Latch.setOperand1(rs1);
			OF_EX_Latch.setOperand2(rs2);
			OF_EX_Latch.setDestinationOperand(rd);
			OF_EX_Latch.setImm(imm);
			OF_EX_Latch.setPC(IF_OF_Latch.getPC());
			OF_EX_Latch.setEX_enable(true);
			IF_EnableLatch.setIF_enable(true);

			if (opcode == 29) {
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setIF_enable(false);
			}

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
