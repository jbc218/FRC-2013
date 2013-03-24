package com.team254.frc2013.commands;

/**
 * @author tombot
 */
public class RunIntakeCommand extends CommandBase {
  double speed;
  boolean runConveyor = true;
  
  public RunIntakeCommand(double speed, boolean runConveyor) {
    this.speed = speed;
    this.runConveyor = runConveyor;
    requires(intake);
    requires(conveyor);
  }
  
  public RunIntakeCommand(double speed) {
    this(speed, true);
  }
  protected void initialize() {
  }

  protected void execute() {
    double tmpSpeed = speed;
    if (!shooter.isIndexerDown()){
      tmpSpeed = 0; // Don't run the conveyor with the indexer in the "up" position
    }
    intake.setIntakePower(tmpSpeed);
    if (runConveyor)
      conveyor.setMotor(tmpSpeed);
    else
      conveyor.setMotor(0);
  }

  protected boolean isFinished() {
    return true;
  }

  protected void end() {
  }

  protected void interrupted() {
  }

}
