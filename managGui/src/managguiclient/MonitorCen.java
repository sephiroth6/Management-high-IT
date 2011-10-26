package managguiclient;
/**
 *
 * @author Angelo
 */
class MonitorCen {
	int x_cent, y_cent;
	
	MonitorCen( int x, int y ){
            this.x_cent = x;
            this.y_cent = y;
		
	}
	
        public void setPX(int x){x_cent = x;}
        public void setPY(int y){y_cent = y;}
        
        public int getPX(){ return x_cent; }
        public int getPY(){ return y_cent; }
}
