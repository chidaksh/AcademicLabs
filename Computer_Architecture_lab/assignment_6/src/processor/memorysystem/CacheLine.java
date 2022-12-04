package processor.memorysystem;

public class CacheLine{
    int[] TagOfDataArray = new int[2]; 
    int[] Data = new int[2];
    int LRU;

    public CacheLine() {
        this.TagOfDataArray[0] = -1;
        this.TagOfDataArray[1] = -1;
        this.LRU = 0;
    }

    public CacheLine(int newLRU) {
        this.LRU = newLRU;
        this.TagOfDataArray[0] = -1;
        this.TagOfDataArray[1] = -1;
    }

    public int getData(int index) {
        return this.Data[index];
    }

    public int getTagOfDataArray(int index) {
        return this.TagOfDataArray[index];
    }

    public int getLRU() {
        return this.LRU;
    }

    public int setLRU(int newLRU) {
        this.LRU = newLRU;
        return this.LRU;
    }

    public void setValue(int TagOfDataArray, int value) {
        if(TagOfDataArray == this.TagOfDataArray[0]) {
            this.Data[0] = value;
            this.LRU = 1;
        }
        else if(TagOfDataArray == this.TagOfDataArray[1]) {
            this.Data[1] = value;
            this.LRU = 0;
        }
        else {
            this.TagOfDataArray[this.LRU] = TagOfDataArray;
            this.Data[this.LRU] = value;
            this.LRU = 1- this.LRU;
        }
	}

    public String toString() {
        return Integer.toString(this.LRU);
    }
}