package codelets.behaviors;

import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import memory.CreatureInnerSense;
import support.Location;
import support.Status;
import ws3dproxy.CommandExecException;
import ws3dproxy.model.Creature;

public class GoToNextStep extends Codelet {

	private MemoryObject pathMO;
        private MemoryObject lastStepMO;
        private MemoryObject statusMO;
	private MemoryObject selfInfoMO;
	private MemoryObject legsMO;
	private int creatureBasicSpeed;
	private double reachDistance;
        private Creature c;
        
	public GoToNextStep(int creatureBasicSpeed, int reachDistance, Creature c) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
                this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
		pathMO=(MemoryObject)this.getInput("PATH");
                lastStepMO=(MemoryObject)this.getInput("LAST");
                statusMO=(MemoryObject)this.getInput("STATUS");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
                legsMO=(MemoryObject)this.getOutput("LEGS");
	}

	@Override
	public void proc() {
            Status status = (Status) statusMO.getI();
            Location lastStep = (Location) lastStepMO.getI();
            List<Location> path = (List<Location>) pathMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
            
            if(status.getPlanStatus() && !status.getGameGoalStatus()){
                synchronized(status){
                    double distance = calculateDistance(cis.position.getX(), cis.position.getY(),700.0,300.0);
                    JSONObject message=new JSONObject();
                    if(distance<reachDistance){
                        message.put("ACTION", "STOP");
                        legsMO.updateI(message.toString());
                        status.setGameGoalStatus(true);
                        System.out.println("Game completed");
                        try 
	                {
	                     Thread.sleep(4000);
	                } catch (Exception e){
	                     e.printStackTrace();
	                }
                        
	                System.exit(1);
                        
                    }else{
                        synchronized(path){
                            Location nextStep = this.getNextStep(c.getPosition().getX(), c.getPosition().getY(), path);
                            if(nextStep!=null){
//                                System.out.println("Creature position: " + c.getPosition().getX() + " x " + c.getPosition().getY());
                                if(lastStep.getX()==nextStep.getX() && lastStep.getY()==nextStep.getY()){
                                    System.out.println("Behaviours > Same step");
                                    legsMO.updateI("");
                                    return;
                                }else{

                                    try {
                                        lastStep.setX(nextStep.getX());
                                        lastStep.setY(nextStep.getY());
                                        lastStepMO.setI(lastStep);
                                        double nextCellX = nextStep.getX()*50;
                                        double nextCellY = nextStep.getY()*50;
    //                                message.put("ACTION", "GOTO");
    //                                message.put("X", nextCellX);
    //                                message.put("Y", nextCellY);
    //                                message.put("SPEED", creatureBasicSpeed);
                                        c.moveto(creatureBasicSpeed, nextCellX, nextCellY);
                                        System.out.println("Behaviours > Next step: " + nextCellX + " x " + nextCellY);
                                    } catch (CommandExecException ex) {
                                        ex.printStackTrace();
                                        Logger.getLogger(GoToNextStep.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            }else{
                                System.out.println("Behaviours > Next step undefined");
                            }
                        }
                    }
                    legsMO.updateI(message.toString());
                }
                
            }
        }//end proc
        
        @Override
        public void calculateActivation() {
        
        }

    private Location getNextStep(double creature_x, double creature_y, List<Location> path) {
    	
            for(int i=0;i < path.size();i++) {

                    double x = creature_x / 50;
                    double y = creature_y / 50;
                    int x_min = path.get(i).getX()-1;
                    int x_max = path.get(i).getX()+1;
                    int y_min = path.get(i).getY()-1;
                    int y_max = path.get(i).getY()+1;

                    if((x >= x_min && x <= x_max) &&
                       (y >= y_min && y <= y_max)) {
                       if(i==path.size()-1) {
                               return path.get(i);
                       }else {
                               return path.get(i+1);
                       }
                    }
            }
            return null;
        }
    
    private double calculateDistance(double creatureX, double creatureY, double thingX, double thingY) {
		
    	Double distance = 0.0;

		if (creatureX > thingX) {
			distance += (creatureX - thingX) * (creatureX - thingX);
		} else {
			distance += (thingX - creatureX) * (thingX - creatureX);
		}

		if (creatureY > thingY) {
			distance += (creatureY - thingY) * (creatureY - thingY);
		} else {
			distance += (thingY - creatureY) * (thingY - creatureY);
		}

		return Math.sqrt(distance);
    }        
}
