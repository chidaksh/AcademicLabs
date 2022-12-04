package generic;

import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;

import generic.Operand.OperandType;

public class Simulator {

	static FileInputStream inputcodeStream = null;
	public static Map<Instruction.OperationType, String> operation_encode = new HashMap<Instruction.OperationType, String>() {
		{
			put(Instruction.OperationType.add, "00000");
			put(Instruction.OperationType.addi, "00001");
			put(Instruction.OperationType.sub, "00010");
			put(Instruction.OperationType.subi, "00011");
			put(Instruction.OperationType.mul, "00100");
			put(Instruction.OperationType.muli, "00101");
			put(Instruction.OperationType.div, "00110");
			put(Instruction.OperationType.divi, "00111");
			put(Instruction.OperationType.and, "01000");
			put(Instruction.OperationType.andi, "01001");
			put(Instruction.OperationType.or, "01010");
			put(Instruction.OperationType.ori, "01011");
			put(Instruction.OperationType.xor, "01100");
			put(Instruction.OperationType.xori, "01101");
			put(Instruction.OperationType.slt, "01110");
			put(Instruction.OperationType.slti, "01111");
			put(Instruction.OperationType.sll, "10000");
			put(Instruction.OperationType.slli, "10001");
			put(Instruction.OperationType.srl, "10010");
			put(Instruction.OperationType.srli, "10011");
			put(Instruction.OperationType.sra, "10100");
			put(Instruction.OperationType.srai, "10101");
			put(Instruction.OperationType.load, "10110");
			put(Instruction.OperationType.store, "10111");
			put(Instruction.OperationType.end, "11101");
			put(Instruction.OperationType.beq, "11001");
			put(Instruction.OperationType.jmp, "11000");
			put(Instruction.OperationType.bne, "11010");
			put(Instruction.OperationType.blt, "11011");
			put(Instruction.OperationType.bgt, "11100");
		}
	};
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

	public static void setupSimulation(String assemblyProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	private static String toBinary(int num, int len) {
		String binary = String.format("%" + len + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}

	private static String convertToBinary(Operand i, int specifiedLength) {
		if (i == null)
			return toBinary(0, specifiedLength);

		if (i.getOperandType() == Operand.OperandType.Label)
			return toBinary(ParsedProgram.symtab.get(i.getLabelValue()), specifiedLength);

		return toBinary(i.getValue(), specifiedLength);
	}

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

	public static int suppliment(Instruction i, String a) {

		String encoded_func = operation_encode.get(i.getOperationType());

		if (a == "R3") {
			String rs1 = convertToBinary(i.getSourceOperand1(), 5);
			String rs2 = convertToBinary(i.getSourceOperand2(), 5);
			String rd = convertToBinary(i.getDestinationOperand(), 5);
			String unused = "000000000000";
			encoded_func = encoded_func + rs1 + rs2 + rd + unused;
		} else if (a == "R2I_0") {
			String rs1 = convertToBinary(i.getSourceOperand1(), 5);
			String rd = convertToBinary(i.getDestinationOperand(), 5);
			String imm = convertToBinary(i.getSourceOperand2(), 17);
			encoded_func = encoded_func + rs1 + rd + imm;
		} else if (a == "R2I_1") {
			String rs1 = convertToBinary(i.getSourceOperand1(), 5);
			String rd = convertToBinary(i.getSourceOperand2(), 5);
			int pc = i.getProgramCounter();
			int value = Integer.parseInt(convertToBinary(i.getDestinationOperand(), 5), 2) - pc;
			String imm = String.format("%" + 17 + "s",
					Integer.toBinaryString(value)).replaceAll(" ", "0");

			encoded_func = encoded_func + rs1 + rd + imm.substring(imm.length() - 17);
		} else if (a == "RI_0") {
			int pc = i.getProgramCounter();
			String rd, imm;
			if (Operand.OperandType.Register == i.destinationOperand.getOperandType()) {
				rd = convertToBinary(i.destinationOperand, 5);
				imm = "0000000000000000000000";
			} else {
				int value = Integer.parseInt(convertToBinary(i.getDestinationOperand(), 5), 2) - pc;
				rd = "00000";
				imm = String.format("%" + 22 + "s",
						Integer.toBinaryString(value)).replaceAll(" ", "0");
				imm = imm.substring(imm.length() - 22);
			}

			encoded_func = encoded_func + rd + imm;
		} else if (a == "RI_1") {
			String unused = "000000000000000000000000000";
			encoded_func = encoded_func + unused;
		}
		int temp = (int) (Long.parseLong(encoded_func, 2));
		return temp;
	}

	public static void assemble(String objectProgramFile) {

		try (
				// 1. open the objectProgramFile in binary mode
				OutputStream file = new BufferedOutputStream(new FileOutputStream(objectProgramFile));) {
			// 2. write the firstCodeAddress to the file
			int firstaddresscode = ParsedProgram.firstCodeAddress;
			byte[] header = ByteBuffer.allocate(4).putInt(firstaddresscode).array();
			file.write(header);
			// 3. write the data to the file
			for (int i : ParsedProgram.data) {
				byte[] datum = ByteBuffer.allocate(4).putInt(i).array();
				file.write(datum);
			}
			// 4. assemble one instruction at a time, and write to the file
			for (Instruction i : ParsedProgram.code) {
				String adf = addressformat(i.operationType);
				int value = suppliment(i, adf);
				byte[] inst = ByteBuffer.allocate(4).putInt(value).array();
				file.write(inst);
			}
			// 5. close the file
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
