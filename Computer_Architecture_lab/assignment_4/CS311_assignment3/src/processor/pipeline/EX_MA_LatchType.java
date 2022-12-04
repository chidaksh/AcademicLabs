package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {

	boolean MA_enable;
	int alu_result;
	Instruction instruction;
	int flag;
	boolean isJunk;

	public EX_MA_LatchType()
	{
		MA_enable = true;
		instruction = null;
		flag=0;
	}
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public int getAluResult() {
		return alu_result;
	}

	public void setAluResult(int result) {
		alu_result = result;
	}

	public void setisJunk(boolean value) {
		this.isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}
}
