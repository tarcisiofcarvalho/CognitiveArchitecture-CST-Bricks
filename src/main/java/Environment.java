/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

import java.util.logging.Level;
import java.util.logging.Logger;
import ws3dproxy.CommandExecException;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

/**
 *
 * @author rgudwin
 */
public class Environment {
    
    public String host="localhost";
    public int port = 4011;
    public String robotID="r0";
    public Creature c = null;
        
    public Environment(){
          WS3DProxy proxy = new WS3DProxy();
          try {   
            
            // --- Word and creature setup --- //
            World w = World.getInstance();
            w.reset();
            c = proxy.createCreature(100,100,0);
            
            // --- Defining the target objective --- //
            CommandUtility.sendNewWaypoint(700.00, 300.00);
              
            // --- Defining Bricks - Case A--- //
//            CommandUtility.sendNewBrick(0, 200, 50, 250, 250);
//            CommandUtility.sendNewBrick(0, 400, 150, 450, 400);

            // --- Defining Bricks - Case B--- //
            CommandUtility.sendNewBrick(0, 200, 50, 250, 250);
            CommandUtility.sendNewBrick(0, 600, 200, 650, 350);
            CommandUtility.sendNewBrick(0, 500, 400, 700, 450);
            CommandUtility.sendNewBrick(0, 400, 150, 450, 400);
            
//            // --- Defining Bricks - Case C--- //
//            CommandUtility.sendNewBrick(0, 200, 50, 250, 250);            
//            CommandUtility.sendNewBrick(0, 500, 400, 700, 450);
            
            System.out.println("Path planning created...");
            
            // --- Staring the creature --- //
            c.start();
             
            // --- Updating the state to reflect the creature leaflets --- //
            c.updateState();
            Thread.sleep(5000);
          } catch (CommandExecException e) {
              e.printStackTrace();
          } catch (InterruptedException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          System.out.println("Robot "+c.getName()+" is ready to go.");
		


	}
}
