/*
 * FIRST Team 254 - The Cheesy Poofs
 * Team 254 Lib
 * Control
 * StateSpaceGains
 *
 * This holds the gains for a State Space Controller.
 */

package com.team254.lib.control;

/**
 *
 * @author Tom Bottiglieri
 */
public class StateSpaceGains implements Gains {
  double[] A;
  double[] B;
  double[] C;
  double[] D;
  double[] L;
  double[] K;
  double[] Umax;
  double[] Umin;
  boolean updated = false;

  public StateSpaceGains(double [] A,
                         double [] B,
                         double [] C,
                         double [] D,
                         double [] L,
                         double [] K,
                         double [] Umax,
                         double [] Umin)
  {
    this.A = A;
    this.B = B;
    this.C = C;
    this.D = D;
    this.L = L;
    this.K = K;
    this.Umax = Umax;
    this.Umin = Umin;
  }

  public double[] getA() {
    return A;
  }

  public double[] getB() {
    return B;
  }

  public double[] getC() {
    return C;
  }

  public double[] getD() {
    return D;
  }

  public double[] getL() {
    return L;
  }

  public double[] getK() {
    return K;
  }

  public double[] getUmax() {
    return Umax;
  }

  public double[] getUmin() {
    return Umin;
  }

  public boolean updated() {
    boolean r = updated;
    updated = false;
    return r;
  }


}
