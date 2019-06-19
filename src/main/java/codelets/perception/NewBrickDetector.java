package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.Status;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;


public class NewBrickDetector extends Codelet {

        private MemoryObject visionMO;
        private MemoryObject bricksMO;
        private MemoryObject statusMO;
        
	public NewBrickDetector(){
		
	}

	@Override
	public void accessMemoryObjects() {
            synchronized(this){
                this.bricksMO=(MemoryObject)this.getInput("BRICKS");
                this.visionMO=(MemoryObject)this.getInput("VISION");
            }
            this.statusMO=(MemoryObject)this.getOutput("STATUS");
	}

	@Override
	public void proc() {
            Status status = (Status) statusMO.getI();
            List<Thing> bricks = Collections.synchronizedList((List<Thing>) bricksMO.getI());
            List<Thing> vision = new CopyOnWriteArrayList((List<Thing>) visionMO.getI()); 
            
            if(status.getPlanStatus() && !status.getGameGoalStatus()){
                synchronized (vision) {
                    for (Thing thing : vision) {
                        if(thing.getCategory()==Constants.categoryBRICK && !this.brickControlCheck(thing,bricks)){
                            status.setPlanStatus(false);
                            statusMO.setI(status);
                            System.out.println("Perception > New Brick Detector");
                        }
                    }
                }
            }
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }
        
    public boolean brickControlCheck(Thing b, List<Thing> brickList) {
    	
    	for(Thing t : brickList) {
        	
    		if(t.getX1()==b.getX1() && t.getY2()==b.getY2())
    			return true;
    	}
    	
    	System.out.println("Brick does not match.....");
    	System.out.println("x1: " + b.getX1());
    	System.out.println("y1: " + b.getY1());
    	
    	System.out.println("x2: " + b.getX2());
    	System.out.println("y2: " + b.getY2());
    	
    	System.out.println("xxxxxxxxx");
    	return false;
    } 

    
}//end class


