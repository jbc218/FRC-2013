/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.frc2013.subsystems.controllers;

import com.team254.frc2013.subsystems.Conveyor;
import com.team254.frc2013.subsystems.Intake;
import com.team254.frc2013.subsystems.Shooter;
import com.team254.lib.control.PeriodicSubsystem;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author tombot
 */
public class ShootController extends PeriodicSubsystem {

  Shooter s;
  Conveyor c;
  Intake i;
  int state = IDLE;
  int oldState = state;
  Timer stateTimer = new Timer();
  // States
  static final int IDLE = 0;
  static final int INTAKE = 1;
  static final int EXHAUST = 2;
  static final int SHOOT_GO_UP = 3;
  static final int SHOOT_WAIT_FOR_SPEED = 4;
  static final int FIX_INDEXER_OUT = 5;
  static final int FIX_INDEXER_IN = 6;
  static final int SHOOT_EXTEND = 7;
  static final int SHOOT_GO_DOWN = 8;
  static final int LOAD_DISC_RAPID = 9;
  static final int FEED_DISC = 10;
  static final int MANUAL_INDEX = 11;
  public boolean wantIntake = false;
  public boolean wantExhaust = false;
  public boolean wantRapidFire = false;
  public boolean wantShoot = false;
  public boolean wantManualIndex = false;
  public int shotCount = 0;
  boolean firstRun = true;
  public boolean wantFeed = false;
  int rapidFireStopShotCount = 4;
  int stopShotCount = 1;
  private boolean firstShot = true;

  public ShootController(Shooter s, Conveyor c, Intake i) {
    this.s = s;
    this.c = c;
    this.i = i;
    stateTimer.start();
  }

  private boolean timedOut(double time) {
    return stateTimer.get() > time;
  }

  public void update() {
    boolean wantIndexerUp = false || firstShot;
    boolean wantExtend = false;
    double intake = 0;
    if (!wantRapidFire) {
      rapidFireStopShotCount = shotCount + 4;
    }
    if (!wantShoot) {
      stopShotCount = shotCount + 1;
    }

   // System.out.println(state + " " + wantManualIndex + " " + wantIntake);
    switch (state) {
      case IDLE:

        if (wantIntake) {
          state = INTAKE;
        }

        if (wantExhaust) {
          state = EXHAUST;
        }

        if (wantManualIndex) {
          state = MANUAL_INDEX;
        }

        if (wantShoot && s.isOn()) {
          if (wantRapidFire && shotCount <= rapidFireStopShotCount) {
            state = LOAD_DISC_RAPID;
          } else if (stopShotCount > shotCount) {
            state = SHOOT_GO_UP;
          }
        }
        if (wantFeed) {
          state = FEED_DISC;
        }
        break;


      case INTAKE:
        intake = 1;
        state = IDLE;
        break;

      case EXHAUST:
        intake = -1;
        state = IDLE;
        break;

      case LOAD_DISC_RAPID:
        intake = 1;
        if (s.isIndexerLoaded()) {
          state = SHOOT_GO_UP;
        }
        if (!wantRapidFire) {
          state = IDLE;
        }
        break;

      case SHOOT_GO_UP:
        wantIndexerUp = true;
        if (s.isIndexerSensedUp()) {
          state = SHOOT_WAIT_FOR_SPEED;
        } else if (timedOut(1.0)) {
          state = FIX_INDEXER_OUT;
        }
        break;

      case SHOOT_WAIT_FOR_SPEED:
        wantIndexerUp = true;
        if (s.onSpeedTarget() || timedOut(.5)) {
          state = SHOOT_EXTEND;
        }
        break;

      case SHOOT_EXTEND:
        if (firstRun) {
          shotCount++;
        }
        wantIndexerUp = true;
        wantExtend = true;
        wantShoot = false;
        if (timedOut(.25)) {
          state = SHOOT_GO_DOWN;
        }
        break;

      case SHOOT_GO_DOWN:
        if (s.isIndexerSensedDown()) {
          System.out.println("sensed down;");
          state = IDLE;
        } else if (timedOut(.4)) {
          state = FIX_INDEXER_OUT;
        }
        break;

      case FIX_INDEXER_OUT:
        intake = -1;
        if (s.isIndexerSensedDown() || timedOut(.4)) {
          state = FIX_INDEXER_IN;
        }
        break;

      case FIX_INDEXER_IN:
        intake = 1;
        if (s.isIndexerLoaded() || timedOut(.5)) {
          state = IDLE;
        }
        break;

      case FEED_DISC:
        intake = 1;
        if (s.isIndexerLoaded()) {
          state = IDLE;
          if (wantShoot) {
            state = SHOOT_GO_UP;
          }
          wantFeed = false;
        }
        if (wantIntake || wantExhaust) {
          state = IDLE;
          wantFeed = false;
        }
        break;

      case MANUAL_INDEX:
        wantIndexerUp = true;
        if (!wantManualIndex) {
          state = SHOOT_GO_DOWN;
        }
        if (wantShoot) {
          state = SHOOT_GO_UP;
        }
        break;
    }

    if (shotCount > 0 || wantManualIndex) {
      firstShot = false;
    }

    if (wantExtend) {
      s.extend();
    } else {
      s.retract();
    }

    s.setIndexerUp(wantIndexerUp);

    if (wantIndexerUp && intake > 0) {
      intake = 0;
    }

    c.setMotor(intake);
    i.setIntakePower(intake);

    firstRun = false;
    if (state != oldState) {
      stateTimer.reset();
      firstRun = true;
    }
    oldState = state;
  }

  protected void initDefaultCommand() {
  }
}