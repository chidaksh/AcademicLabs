package processor.pipeline;

public class IF_EnableLatchType {

	boolean IF_enable = true;
	boolean isJunk;

	public IF_EnableLatchType() {
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setisJunk(boolean value){
		isJunk = value;
	}

	public boolean getisJunk(){
		return isJunk;
	}
}
