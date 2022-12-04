package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {

	boolean EX_enable;
	Instruction instruction;
	boolean isJunk;

	public OF_EX_LatchType()
	{
		EX_enable = true;
		instruction = null;
		isJunk = true;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}

	public void setisJunk(boolean value) {
		isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}
}
