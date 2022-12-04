package processor.pipeline;

public class IF_OF_LatchType {

	boolean OF_enable;
	int instruction;
	int PC;
	boolean isBusy;
	boolean isJunk;

	public IF_OF_LatchType() {
		OF_enable = false;
		PC = -1;
		instruction = -1999;
		isBusy = false;
	}

	public IF_OF_LatchType(boolean oF_enable, boolean isBusy) {
		OF_enable = oF_enable;
		PC = -1;
		instruction = -1999;
		this.isBusy = isBusy;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public boolean checkInstruction(int instruction) {
		return this.instruction == instruction;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int newPC) {
		PC = newPC;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean getisBusy() {
		return isBusy;
	}

	public void setisBusy(boolean checkbusy) {
		isBusy = checkbusy;
	}

	public void setisJunk(boolean value) {
		this.isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}
}
