public class infiltrator extends border {
    int x;
    int y;

    public infiltrator(int length, int width,float p) {
        super(length, width,p);
        x = -1;
        y = (length - 1) / 2;
    }

    public void movement() {
        if (x == -1) {
            if (!this.boundary[this.x + 1][this.y].sensor_on) {
                this.x = this.x + 1;
            } else if (!this.boundary[this.x + 1][this.y - 1].sensor_on) {
                this.x = this.x + 1;
                this.y = this.y - 1;
            } else if (!this.boundary[this.x + 1][this.y + 1].sensor_on) {
                this.x = this.x + 1;
                this.y = this.y + 1;
            }
        }
        else if(x==this.w-1){
            if(!this.boundary[this.x][this.y].sensor_on){
                this.x=this.x+1;
            }
        }
        else if (!this.boundary[this.x][this.y].sensor_on) {
            if (!this.boundary[this.x + 1][this.y].sensor_on) {
                this.x = this.x + 1;
            } else if (y-1>=0) {
                if(!this.boundary[this.x + 1][this.y - 1].sensor_on){
                    this.x = this.x + 1;
                    this.y = this.y - 1;
                }
                
            } else if (y+1<this.l) {
                if(!this.boundary[this.x + 1][this.y + 1].sensor_on){
                    this.x = this.x + 1;
                    this.y = this.y + 1;
                }
            }
        }
        //System.out.println("current position :" + this.x + ", " + this.y);
    }

    public boolean goal() {
        if (this.x == this.w) {
            return true;
        } else {
            return false;
        }
    }
}
