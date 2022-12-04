package processor.pipeline;

import generic.Instruction.OperationType;
import generic.Operand;

public class OF_EX_LatchType {

	boolean EX_enable;
	Operand sourceoperand1;
	Operand sourceoperand2;
	Operand destinationoperand;
	OperationType operation;
	String EncodedType;
	int Immediate;
	boolean isImmediate;

	public OF_EX_LatchType() {
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setrs1(Operand rs1) {
		sourceoperand1 = rs1;
	}

	public void setrs2(Operand rs2) {
		sourceoperand2 = rs2;
	}

	public void setrd(Operand rd) {
		destinationoperand = rd;
	}

	public void setoperation(OperationType op) {
		operation = op;
	}

	public void setImmediate(int imm) {
		Immediate = imm;
	}

	public void setEncodedType(String encd) {
		EncodedType = encd;
	}

	public void setisImmediatetrue() {
		isImmediate = true;
	}

	public void setisImmediatefalse() {
		isImmediate = false;
	}

	public Operand getrs1() {
		return sourceoperand1;
	}

	public Operand getrs2() {
		return sourceoperand2;
	}

	public Operand getrd() {
		return destinationoperand;
	}

	public OperationType getoperation() {
		return operation;
	}

	public String getEncodedType() {
		return EncodedType;
	}

	public int getImmediate() {
		return Immediate;
	}

	public boolean getisImmediate() {
		return isImmediate;
	}
}
