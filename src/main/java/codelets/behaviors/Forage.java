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

package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import org.json.JSONException;
import org.json.JSONObject;
import support.Status;

public class Forage extends Codelet {
    
        private MemoryObject statusMO;
        private MemoryObject legsMO;
        
	/**
	 * Default constructor
	 */
	public Forage(){       
	}
 
	@Override
	public void accessMemoryObjects() {
            statusMO = (MemoryObject) this.getInput("STATUS");
            legsMO=(MemoryObject)this.getOutput("LEGS");	
	}
        
	@Override
	public void proc() {
            Status status = (Status) statusMO.getI();
            if(!status.getPlanStatus() && !status.getFirstRun()){
		JSONObject message=new JSONObject();
                try {
                        message.put("ACTION", "FORAGE");
                        legsMO.updateI(message.toString());
                        System.out.println("Behaviours > Forage");

                } catch (JSONException e) {
                        e.printStackTrace();
                }                
            }		
	}
        
        @Override
        public void calculateActivation() {
            
        }


}
