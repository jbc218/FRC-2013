package com.team254.frc2013.control;

import java.util.Vector;

/**
 * Base class for all controllers.
 *
 * @author richard@tean254.com (Richard Lin)
 */
public abstract class Controller {
  private static Vector controllers = new Vector();
  private String name;
  boolean enabled;
  
  public Controller(String name) {
    controllers.addElement(this);
    this.name = name;
  }
  
  public String getName(){
    return name;
  }
  public static void updateAll() {
    for(int i = 0; i < controllers.size(); i++) {
      Controller c = (Controller) controllers.elementAt(i);
      c.update();
    }
  }
  
  public void enable() {
    enabled = true;
  }
  
  public void disable() {
    enabled = false;
  }
  
  public boolean isEnabled() {
    return enabled;
  }
  
  public abstract void update();
  
  public abstract void setGoal(double goal);
}
