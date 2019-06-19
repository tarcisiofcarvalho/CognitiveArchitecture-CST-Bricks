package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import memory.CreatureInnerSense;
import support.AStarSupport;
import support.GridMap;
import support.Location;
import support.Status;
import ws3dproxy.CommandUtility;
import ws3dproxy.model.Thing;


public class PlanDetector extends Codelet {

        private MemoryObject statusMO;
        private MemoryObject innerMO;
        private MemoryObject bricksMO;
        private MemoryObject pathMO;

        private List<Thing> brickList;
        private GridMap grid;
        private List<Location> locList;
        private AStarSupport astar;
        
	public PlanDetector(){
		
	}

	@Override
	public void accessMemoryObjects() {
            synchronized(this){
                this.statusMO=(MemoryObject)this.getInput("STATUS");
                this.innerMO=(MemoryObject)this.getInput("INNER");
            }
		this.pathMO=(MemoryObject)this.getOutput("PATH");
                this.bricksMO=(MemoryObject)this.getOutput("BRICKS");
	}

	@Override
	public void proc() {
            Status status = (Status) statusMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) innerMO.getI();
            List<Location> path = Collections.synchronizedList((List<Location>) pathMO.getI());
            brickList = Collections.synchronizedList((List<Thing>) bricksMO.getI());
            
            if(!status.getPlanStatus()){
                
                synchronized (path) { 
                    
                    // -- Generating the grid map location -- //
                    locList = new ArrayList<>();
                    for(int xA=1;xA<=16;xA++){ // x loop
                        for(int yA=1;yA<=16;yA++){ // y loop
                            locList.add(new Location(xA, yA, xA*50, yA*50)); // creating grid
                        }
                    }
                    // -- Generate the creature path to the target -- //
                    List<Location> walls = this.getWalls(locList);
                    
                    // -- Create creature path -- //
                    path = this.createCreaturePath(cis.position.getX(),cis.position.getY(), walls, locList);
                    
                    System.out.println("Perception > Path planning created... size > " + path.size());
                    
                    status.setPlanStatus(true);
                    
                    status.setFirstRun(false);
                }
                // --- Memory Object Update --- //
                statusMO.setI(status);
                pathMO.setI(path);   
                bricksMO.setI(brickList);
            }
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }
        
    private List<Location> createCreaturePath(double x, double y, List<Location> walls, List<Location> locList) {
            
        // --- Creating the Grid map cells --- //
        grid = new GridMap(16, 16, locList, walls);
        System.out.println("Grid map created...");

        // --- Defining the best Creature path, to avoid bricks and reach the target objective --- //
        astar = new AStarSupport();
        Location target = getGridThingPosition(700, 300, locList);
        return astar.AStarSearch(grid, getGridThingPosition(x,y,locList), target);  
        
    }
    
    private List<Location> getWalls(List<Location> cells){
    	
        List<Location> walls = new ArrayList<Location>();

        brickList = new ArrayList<Thing>();

        try {
        StringTokenizer stW = CommandUtility.sendGetWorldEntities();

        // -- Iterate over tokens looking for Brick things -- //
        while(stW.hasMoreTokens()) {

            String temp_token = stW.nextToken();

            // -- Check if the next token is related to Brick thing -- /
            if(temp_token.length()>6) {
                if(temp_token.substring(0, 6).equals("Brick_")) {

                    // -- skip first two tokens that are not related to X and Y position -- //
                    stW.nextToken();	
                    stW.nextToken();

                            // -- Identify brick edges and parse it to grid cells--- //
                    double x_A = Double.parseDouble(stW.nextToken());
                    double x_B = Double.parseDouble(stW.nextToken());
                    double y_A = Double.parseDouble(stW.nextToken());
                    double y_B = Double.parseDouble(stW.nextToken());

                    Location edgeA = this.getGridThingPosition(x_A, y_A, cells);
                    Location edgeB = this.getGridThingPosition(x_B, y_B, cells);

                    //Adding on Brick List Control
                    brickList.add(new Thing("Brick",1,x_A,y_A,x_B,y_B,0.0,0.0,"Red",0.0,0.0));

                    // -- Identify the number of x and y positions the brick covers -- //
                    int xStart = 0;
                    int xEnd = 0;
                    int yStart = 0;
                    int yEnd = 0;

                    if(edgeB.getX()>=edgeA.getX()) {
                            xStart=edgeA.getX();
                            xEnd=edgeB.getX();
                    }else {
                            xStart=edgeB.getX();
                            xEnd=edgeA.getX();            				
                    }

                    if(edgeB.getY()>=edgeA.getY()) {
                            yStart=edgeA.getY();
                            yEnd=edgeB.getY();
                    }else {
                            yStart=edgeB.getY();
                            yEnd=edgeA.getY();            				
                    }

                    // -- Generating grid occupied cells based on bricks body -- //
                    for(int x=xStart;x<=xEnd;x++) {
                            for(int y=yStart;y<=yEnd;y++) {
                                    walls.add(new Location(x, y, x*50, y*50));
                                    System.out.println("Occupied: " + x + " x " + y);
                            }
                    }
                }
            }
        } 

        }catch (Exception e) {
                walls = null;
                e.printStackTrace();
        }

        return walls;
    }
    
    public Location getGridThingPosition(double x, double y, List<Location> cells){
        for (Location cell : cells) {
            if( (x <= cell.getxRange() && x>= (cell.getxRange()-50))
               && (y <= cell.getyRange() && y>= (cell.getyRange()-50))){
                return cell;
            }
        }
        return null;
    }     

}//end class


