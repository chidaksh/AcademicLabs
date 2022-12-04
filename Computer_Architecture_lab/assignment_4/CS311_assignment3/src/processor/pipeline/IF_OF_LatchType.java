package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean isJunk;

	public IF_OF_LatchType()
	{
		OF_enable = true;
		instruction = 999999999;
		isJunk = true;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public void setisJunk(boolean value){
		isJunk = value;
	}

	public boolean getisJunk(){
		return isJunk;
	}
	
}
