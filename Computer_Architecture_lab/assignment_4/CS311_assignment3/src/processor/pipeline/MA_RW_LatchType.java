package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {

	boolean RW_enable;
	Instruction instruction;
	int load_output;
	int alu_output;
	boolean isJunk;

	public MA_RW_LatchType()
	{
		RW_enable = true;
		instruction = null;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction cmd) {
		instruction = cmd;
	}

	public void setLoadOutput(int output) {
		this.load_output = output;
	}

	public int getLoadOutput() {
		return load_output;
	}

	public int getAluOutput() {
		return alu_output;
	}

	public void setAluOutput(int output) {
		alu_output = output;
	}

	public void setisJunk(boolean value) {
		isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}
}
