/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

/**
 *
 * @author tfc
 */
public class Status {
    
    private boolean gameGoalstatus;
    private boolean planStatus;
    private boolean firstRun;
    
    public Status(){
        this.gameGoalstatus = false;
        this.planStatus = false;
        this.firstRun = true;
    }
    
    public boolean getFirstRun(){
        return this.firstRun;
    }
 
    public void setFirstRun(boolean firstRun){
        this.firstRun = firstRun;
    }
        
    public void setGameGoalStatus(boolean status){
        this.gameGoalstatus = status;
    }
    
    public boolean getGameGoalStatus(){
        return this.gameGoalstatus;
    }

    public void setPlanStatus(boolean planStatus){
        this.planStatus = planStatus;
    }
    
    public boolean getPlanStatus(){
        return this.planStatus;
    }
}
