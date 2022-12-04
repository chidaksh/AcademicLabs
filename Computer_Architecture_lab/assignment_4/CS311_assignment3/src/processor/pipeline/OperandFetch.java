package processor.pipeline;

import generic.Instruction;
import java.util.*;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import generic.Statistics;
import processor.Processor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public boolean CheckHazard(Instruction inst, Instruction toCheck) {
		if (toCheck != null && !toCheck.getOperationType().toString().equals("nop")
				&& !toCheck.getOperationType().toString().equals("end")
				&& !inst.getOperationType().toString().equals("nop")
				&& !inst.getOperationType().toString().equals("end")
				&& !inst.getOperationType().toString().equals("jmp")
				&& toCheck.getDestinationOperand() != null
				&& (toCheck.getDestinationOperand().getValue() == inst.getSourceOperand1().getValue() ||
						toCheck.getDestinationOperand().getValue() == inst.getSourceOperand2().getValue())) {
			return true;
		} else if (toCheck != null && !toCheck.getOperationType().toString().equals("nop")
				&& !toCheck.getOperationType().toString().equals("end")
				&& !inst.getOperationType().toString().equals("nop")
				&& !inst.getOperationType().toString().equals("end")
				&& !inst.getOperationType().toString().equals("jmp")
				&& toCheck.getDestinationOperand() != null
				&& (toCheck.getDestinationOperand().getValue() == inst.getSourceOperand1().getValue() ||
						toCheck.getDestinationOperand().getValue() == inst.getSourceOperand2().getValue())
				&& (addressformat(inst.getOperationType()) == "R2I_1")) {
			return true;
		} else {
			return false;
		}

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

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType if_enableLatch) {

		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = if_enableLatch;
	}

	public void performOF() {
		int inst = 999999999;
		try {
			inst = IF_OF_Latch.getInstruction();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (inst != 999999999) {
			OperationType[] operationType = OperationType.values();
			String binst = Integer.toBinaryString(inst);
			if (binst.length() != 32) {
				int len = binst.length();
				String to_add = "";
				if ((32 - len) != 0) {
					String s = "0";
					int left = 32 - len;
					to_add = IntStream.range(0, left).mapToObj(i -> s).collect(Collectors.joining(""));
				}
				binst = to_add + binst;
			}
			String opcode = binst.substring(0, 5);
			int opcodei = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[opcodei];
			String encoded_type = addressformat(operation);
			Instruction instn = new Instruction();
			Operand rs1;
			int dec_num;
			Operand rs2;
			Operand rd;
			String str;
			int val;
			if (encoded_type == "R3") {
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(5, 10), 2);
				rs1.setValue(dec_num);

				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(10, 15), 2);
				rs2.setValue(dec_num);

				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(15, 20), 2);
				rd.setValue(dec_num);

				instn.setOperationType(operationType[opcodei]);
				instn.setSourceOperand1(rs1);
				instn.setSourceOperand2(rs2);
				instn.setDestinationOperand(rd);
			} else if (encoded_type == "RI_1") {
				instn.setOperationType(operationType[opcodei]);
			} else if (encoded_type == "RI_0") {
				Operand op = new Operand();
				str = binst.substring(10, 32);
				val = Integer.parseInt(str, 2);
				if (str.charAt(0) == '1') {
					str = twosComplement(str);
					val = Integer.parseInt(str, 2) * -1;
				}
				if (val != 0) {
					op.setOperandType(OperandType.Immediate);
					op.setValue(val);
				} else {
					dec_num = Integer.parseInt(binst.substring(5, 10), 2);
					op.setOperandType(OperandType.Register);
					op.setValue(dec_num);
				}

				instn.setOperationType(operationType[opcodei]);
				instn.setDestinationOperand(op);
				OperandType opt = instn.getDestinationOperand().getOperandType();
				int immediate = 0;
				if (opt == OperandType.Register) {
					immediate = containingProcessor.getRegisterFile().getValue(
							instn.getDestinationOperand().getValue());
				} else {
					immediate = instn.getDestinationOperand().getValue();
				}
				int PC = containingProcessor.getRegisterFile().programCounter - 1;
				int output = immediate + PC;
				containingProcessor.getRegisterFile().setProgramCounter(output);
			} else if (encoded_type == "R2I_1") {
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(5, 10), 2);
				rs1.setValue(dec_num);

				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(10, 15), 2);
				rs2.setValue(dec_num);

				rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				str = binst.substring(15, 32);
				val = Integer.parseInt(str, 2);
				if (str.charAt(0) == '1') {
					str = twosComplement(str);
					val = Integer.parseInt(str, 2) * -1;
				}
				rd.setValue(val);

				instn.setOperationType(operationType[opcodei]);
				instn.setSourceOperand1(rs1);
				instn.setSourceOperand2(rs2);
				instn.setDestinationOperand(rd);
			} else {
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(5, 10), 2);
				rs1.setValue(dec_num);

				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				dec_num = Integer.parseInt(binst.substring(10, 15), 2);
				rd.setValue(dec_num);

				rs2 = new Operand();
				rs2.setOperandType(OperandType.Immediate);
				str = binst.substring(15, 32);
				val = Integer.parseInt(str, 2);
				if (str.charAt(0) == '1') {
					str = twosComplement(str);
					val = Integer.parseInt(str, 2) * -1;
				}
				rs2.setValue(val);

				instn.setOperationType(operationType[opcodei]);
				instn.setSourceOperand1(rs1);
				instn.setSourceOperand2(rs2);
				instn.setDestinationOperand(rd);
			}

			Instruction nop = new Instruction();
			nop.setOperationType(OperationType.nop);
			boolean noConflict = false;
			// System.out.println(CheckHazard(instn, OF_EX_Latch.instruction));
			// System.out.println(CheckHazard(instn, EX_MA_Latch.instruction));
			// System.out.println(CheckHazard(instn, MA_RW_Latch.instruction));

			while (true) {

				if (EX_MA_Latch.getFlag() == 1) {
					OF_EX_Latch.setInstruction(nop);
					containingProcessor.getRegisterFile()
							.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() - 1);
					containingProcessor.setWrong_input(containingProcessor.getWrong_input() + 1);
					EX_MA_Latch.setFlag(0);
					Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() - 1);
					break;
				} else if (CheckHazard(instn, OF_EX_Latch.instruction)
						&& OF_EX_Latch.instruction.getOperationType() != OperationType.nop) {
					IF_EnableLatch.setIF_enable(false);
					OF_EX_Latch.setInstruction(nop);
					containingProcessor.setStalls(containingProcessor.getStalls() + 1);
					Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() - 1);
					break;
				} else if (CheckHazard(instn, EX_MA_Latch.instruction)
						&& EX_MA_Latch.instruction.getOperationType() != OperationType.nop) {
					IF_EnableLatch.setIF_enable(false);
					OF_EX_Latch.setInstruction(nop);
					containingProcessor.setStalls(containingProcessor.getStalls() + 1);
					Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() - 1);
					break;
				} else if (CheckHazard(instn, MA_RW_Latch.instruction)
						&& MA_RW_Latch.instruction.getOperationType() != OperationType.nop) {
					IF_EnableLatch.setIF_enable(false);
					OF_EX_Latch.setInstruction(nop);
					containingProcessor.setStalls(containingProcessor.getStalls() + 1);
					Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() - 1);
					break;
				} else if (instn.getOperationType() == OperationType.end) {
					containingProcessor.getRegisterFile().setFreezed(true);
					containingProcessor.getRegisterFile()
							.setFreezedprogramCounter(containingProcessor.getRegisterFile().getProgramCounter());
					containingProcessor.setFreezed_stalls(containingProcessor.getStalls());
					containingProcessor.setFreezed_wrong_input(containingProcessor.getWrong_input());
					Statistics.setFreezednumberOfInstructions(Statistics.getNumberOfInstructions());
				}
				noConflict = true;
				break;

			}
			if (noConflict && IF_OF_Latch.isOF_enable()) {
				IF_EnableLatch.setIF_enable(true);

				OF_EX_Latch.setInstruction(instn);
			}
		}
	}

}
