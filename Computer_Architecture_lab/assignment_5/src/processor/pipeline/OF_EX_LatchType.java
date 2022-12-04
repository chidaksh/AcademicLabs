package processor.pipeline;

public class OF_EX_LatchType {

	boolean EX_enable;
	String opcode;
	int rs1, rs2, rd, imm;
	int rs1addr, rs2addr;
	int PC;
	boolean isNop;
	boolean isBusy;
	boolean NOP;
	boolean isJunk;

	public OF_EX_LatchType() {
		EX_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		PC = -1;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	public String toString() {
		return "OF_EX_LatchType";
	}

	public boolean comparePC(int pc) {
		return PC == pc;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public boolean getIsNOP() {
		return NOP;
	}

	public void setIsNOP(boolean bool) {
		NOP = bool;
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

	public void setisJunk(boolean value) {
		this.isJunk = value;
	}

	public boolean getisJunk() {
		return isJunk;
	}
}
