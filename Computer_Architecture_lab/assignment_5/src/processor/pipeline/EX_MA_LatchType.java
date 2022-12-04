package processor.pipeline;

public class EX_MA_LatchType {

	boolean MA_enable;
	int aluResult;
	int rs1, rs2, rd, imm;
	int rs1addr, rs2addr;
	String opcode;
	int PC;
	boolean isNop;
	boolean isBusy;
	boolean isJunk;

	public EX_MA_LatchType() {
		MA_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		aluResult = 70000;
		PC = -1;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public boolean getisBusy() {
		return isBusy;
	}

	public void setisBusy(boolean checkbusy) {
		isBusy = checkbusy;
	}

	public void setOperand1(int n) {
		rs1 = n;
	}

	public void setOperand2(int n) {
		rs2 = n;
	}

	public void setDestinationOperand(int n) {
		rd = n;
	}

	public void setImm(int n) {
		imm = n;
	}

	public void setPC(int pc) {
		PC = pc;
	}

	public void setOpcode(String code) {
		opcode = code;
	}

	public void setIsNop(boolean bool) {
		isNop = bool;
	}

	public int getOperand1() {
		return rs1;
	}

	public int getOperand2() {
		return rs2;
	}

	public int getDestinationOperand() {
		return rd;
	}

	public int getImm() {
		return imm;
	}

	public int getPC() {
		return PC;
	}

	public String getOpcode() {
		return opcode;
	}

	public boolean getIsNop() {
		return isNop;
	}

	public void setAluResult(int result) {
		aluResult = result;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setisJunk(boolean value) {
		this.isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}

}
