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

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.Forage;
import codelets.behaviors.GoToNextStep;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.NewBrickDetector;
import codelets.perception.PlanDetector;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.Location;
import support.Status;
import support.MindView;
import ws3dproxy.model.Thing;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {
    
    private static int creatureBasicSpeed=2;
    private static int reachDistance=50;
    
    public AgentMind(Environment env) {
                super();
                
                // Declare Memory Objects
	        MemoryObject legsMO;
	        MemoryObject handsMO;
                MemoryObject visionMO;
                MemoryObject innerSenseMO;
                MemoryObject statusMO;
                MemoryObject pathMO;
                MemoryObject bricksMO;
                MemoryObject lastStepMO;
                
                //Initialize Memory Objects
                
                legsMO=createMemoryObject("LEGS", "");
                
		handsMO=createMemoryObject("HANDS", "");
                
                List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
		visionMO=createMemoryObject("VISION",vision_list);
                
                CreatureInnerSense cis = new CreatureInnerSense();
		innerSenseMO=createMemoryObject("INNER", cis);
                
                Status status = new Status();
                statusMO=createMemoryObject("STATUS", status);
                
                List<Location> path = new ArrayList<Location>();
                pathMO=createMemoryObject("PATH", path);

                List<Location> bricks = new ArrayList<Location>();
                bricksMO=createMemoryObject("BRICKS", bricks);
                
                Location lastStep = new Location(0, 0);
                lastStepMO=createMemoryObject("LAST", lastStep);
                
                // Create and Populate MindViewer
                MindView mv = new MindView("MindView");
                mv.addMO(visionMO);
                mv.addMO(innerSenseMO);
                mv.addMO(handsMO);
                mv.addMO(legsMO);
                mv.addMO(statusMO);
                mv.addMO(pathMO);
                mv.addMO(bricksMO);
                mv.addMO(lastStepMO);
                mv.StartTimer();
                mv.setVisible(true);
		
		/******************************************/
                /********* Create Sensor Codelets *********/
                /******************************************/	
		Codelet vision=new Vision(env.c);
		vision.addOutput(visionMO);
                insertCodelet(vision); //Creates a vision sensor
		
		Codelet innerSense=new InnerSense(env.c);
		innerSense.addOutput(innerSenseMO);
                insertCodelet(innerSense); //A sensor for the inner state of the creature
		
		/******************************************/
                /******** Create Actuator Codelets ********/
                /******************************************/
		Codelet legs=new LegsActionCodelet(env.c);
		legs.addInput(legsMO);
                legs.addInput(handsMO);
                insertCodelet(legs);

		Codelet hands=new HandsActionCodelet(env.c);
		hands.addInput(handsMO);
                insertCodelet(hands);
		
		/******************************************/
                /******* Create Perception Codelets *******/
                /******************************************/
                // --- Game Goal Achieved --- //
                Codelet planDetector = new PlanDetector();
                planDetector.addInput(innerSenseMO);
                planDetector.addInput(statusMO);
                planDetector.addOutput(pathMO);
                planDetector.addOutput(bricksMO);                
                insertCodelet(planDetector);

                // --- New Brick Detector --- //
                Codelet newBrickDetector = new NewBrickDetector();
                newBrickDetector.addInput(visionMO);
                newBrickDetector.addInput(bricksMO);
                newBrickDetector.addOutput(statusMO);                
                insertCodelet(newBrickDetector);
                
		/******************************************/
                /******* Create Behavior Codelets *********/
                /******************************************/               
                Codelet forage=new Forage();
                forage.addInput(statusMO);
                forage.addOutput(legsMO);
                insertCodelet(forage);
                
                Codelet goToNextStep =new GoToNextStep(creatureBasicSpeed,reachDistance,env.c);
                goToNextStep.addInput(statusMO);
                goToNextStep.addInput(lastStepMO);
                goToNextStep.addInput(pathMO);
                goToNextStep.addInput(innerSenseMO);
                goToNextStep.addOutput(legsMO);
                insertCodelet(goToNextStep);
                
                // sets a time step for running the codelets to avoid heating too much your machine
                for (Codelet c : this.getCodeRack().getAllCodelets())
                    c.setTimeStep(100);
		
		// Start Cognitive Cycle
		start(); 
    }             
    
}
