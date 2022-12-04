package processor.pipeline;

import generic.Operand;

public class MA_RW_LatchType {

	boolean RW_enable;
	boolean isWrite;
	boolean isLoad;
	int alu_result;
	int loadresult;
	boolean isEnd;
	Operand DestinationOperand;

	public MA_RW_LatchType() {
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setisWrite(boolean set) {
		isWrite = set;
	}

	public void setAluResult(int alu) {
		alu_result = alu;
	}

	public void setLoadResult(int load) {
		loadresult = load;
	}

	public void setDestinationOperand(Operand destination) {
		DestinationOperand = destination;
	}

	public void setisLoad(boolean isload) {
		isLoad = isload;
	}

	public void setisEnd(boolean end) {
		isEnd = end;
	}

	public boolean getisWrite() {
		return isWrite;
	}

	public int getAluResult() {
		return alu_result;
	}

	public int getLoadResult() {
		return loadresult;
	}

	public Operand getDestinationOperand() {
		return DestinationOperand;
	}

	public boolean getisLoad() {
		return isLoad;
	}

	public boolean getisEnd() {
		return isEnd;
	}
}
