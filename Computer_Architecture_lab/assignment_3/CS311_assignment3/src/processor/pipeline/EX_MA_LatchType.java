package processor.pipeline;

import generic.Operand;

public class EX_MA_LatchType {

	boolean MA_enable;
	int alu_result;
	Operand DestinationOperand;
	boolean isLoad = false;
	boolean isStore = false;
	boolean isWrite;
	boolean isEnd;
	Operand SourceOperand1;

	public EX_MA_LatchType() {
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setALU(int result) {
		alu_result = result;
	}

	public void setDestinationOperand(Operand rd) {
		DestinationOperand = rd;
	}

	public void setisWrite(boolean set) {
		isWrite = set;
	}

	public void setisLoad(boolean check) {
		isLoad = check;
	}

	public void setisStore(boolean check) {
		isStore = check;
	}

	public void setrs1(Operand source) {
		SourceOperand1 = source;
	}

	public void setisEnd(boolean end) {
		isEnd = end;
	}

	public int getALU() {
		return alu_result;
	}

	public Operand getDestinationOperand() {
		return DestinationOperand;
	}

	public boolean getisWrite() {
		return isWrite;
	}

	public boolean getisLoad() {
		return isLoad;
	}

	public boolean getisStore() {
		return isStore;
	}

	public Operand getrs1() {
		return SourceOperand1;
	}

	public boolean getisEnd() {
		return isEnd;
	}
}
