package processor.pipeline;

public class EX_IF_LatchType {
	boolean branch_taken;
	int PC;

	public EX_IF_LatchType() {
		branch_taken = false;
	}

	public void setisBranchTaken(boolean enable) {
		branch_taken = enable;
	}

	public void setProgramCounter(int newPC) {
		PC = newPC;
	}

	public boolean getisBranchTaken() {
		return branch_taken;
	}

	public int getProgramCounter() {
		return PC;
	}

}
