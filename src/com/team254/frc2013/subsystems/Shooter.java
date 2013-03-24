package com.team254.frc2013.subsystems;

import com.team254.frc2013.Constants;
import com.team254.lib.util.Debouncer;
import com.team254.lib.util.ThrottledPrinter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Class representing the shooter wheels, managing its motors and sensors.
 *
 * @author richard@team254.com (Richard Lin)
 * @author tom@team254.com (Tom Bottiglieri)
 * @author eric.vanlare14@bcp.org (Eric van Lare)
 * @author eliwu26@gmail.com (Elias Wu)
 */
public class Shooter extends Subsystem  {
  public static final int PRESET_BACK_PYRAMID = 0;
  public static final int PRESET_FRONT_PYRAMID = 1;
  public static final int PRESET_PYRAMID_GOAL = 2;

  private Talon frontMotor = new Talon(Constants.frontShooterPort.getInt());
  private Talon backMotor = new Talon(Constants.backShooterPort.getInt());
  private Solenoid loader = new Solenoid(Constants.shooterLoaderPort.getInt());
  private Solenoid angle = new Solenoid(Constants.shooterAnglePort.getInt());

  private Solenoid indexerLeft = new Solenoid(Constants.indexerLeftPort.getInt());
  private Solenoid indexerRight = new Solenoid(Constants.indexerRightPort.getInt());
  Debouncer debouncer = new Debouncer(.125);
  ThrottledPrinter p = new ThrottledPrinter(.1);
  public DigitalInput indexerDownSensorA =
      new DigitalInput(Constants.indexerDownSensorPortA.getInt());
  public DigitalInput indexerDownSensorB =
      new DigitalInput(Constants.indexerDownSensorPortB.getInt());

  private double frontPower;
  private double backPower;
  private boolean shooterOn;
  private int loadState = 0;
  private boolean wantShoot = false;
  private Timer stateTimer = new Timer();

  public void setIndexerUp(boolean up) {
    indexerLeft.set(!up);
    indexerRight.set(!up);
  }

  // Load a frisbee into shooter by retracting the piston
  public void retract() {
    loader.set(false);
  }

  // Shoot already loaded frisbee by extending the piston
  public void extend() {
    loader.set(true);
  }

  public boolean tryShoot() {
    //if (loadState == 0) {
    //  wantShoot = true;
    //}
    //return loadState == 0;
    return false;
  }

  public void setHighAngle(boolean high) {
    angle.set(!high);
  }

  public boolean isHighAngle() {
    return angle.get();
  }

  public boolean getLoaderState() {
    return loader.get();
  }

  public boolean isIndexerDown() {
    // The sensor reads true when the indexer is down.
    return indexerLeft.get() && indexerRight.get();
  }

  public Shooter() {
    super();
    setPreset(PRESET_FRONT_PYRAMID);
    shooterOn = false;
    stateTimer.start();
  }

  public void setPreset(int preset) {
    switch (preset) {
      case PRESET_FRONT_PYRAMID:
        setHighAngle(true);
        setPowers(1, 1);
        break;
      case PRESET_PYRAMID_GOAL:
        setHighAngle(true);
        setPowers(0.8, 0.8);
        break;
      case PRESET_BACK_PYRAMID:
      default:
        setHighAngle(false);
        setPowers(1, 1);
    }
  }

  private void setPowers(double frontPower, double backPower) {
    this.frontPower = (frontPower < 0) ? 0 : frontPower;
    this.backPower = (backPower < 0) ? 0 : backPower;
    setShooterOn(shooterOn);
  }

  public void setShooterOn(boolean isOn) {
    shooterOn = isOn;
    if (isOn) {
      frontMotor.set(-frontPower);
      backMotor.set(-backPower);
    } else {
      frontMotor.set(0);
      backMotor.set(0);
    }
  }

  protected void initDefaultCommand() {
  }
}
