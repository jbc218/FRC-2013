package com.team254.frc2013.auto;

import com.team254.frc2013.commands.IntakeRaiseCommand;
import com.team254.frc2013.commands.LoadAndShootCommand;
import com.team254.frc2013.commands.ResetDriveEncodersCommand;
import com.team254.frc2013.commands.ResetGyroCommand;
import com.team254.frc2013.commands.RunIntakeCommand;
import com.team254.frc2013.commands.ShootCommand;
import com.team254.frc2013.commands.ShooterOnCommand;
import com.team254.frc2013.commands.ShooterPresetCommand;
import com.team254.frc2013.commands.WaitCommand;
import com.team254.frc2013.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Shoots two preload discs from the front of the pyramid
 *
 * @author jonathan.chang13@gmail.com (Jonathan Chang)
 */
public class TwoDiscAutoMode extends CommandGroup {
  public TwoDiscAutoMode() {
    addSequential(new ResetDriveEncodersCommand());
    addSequential(new ResetGyroCommand());
    addSequential(new ShooterOnCommand(true));
    addSequential(new IntakeRaiseCommand(IntakeRaiseCommand.INTAKE_DOWN));
    addSequential(new ShooterPresetCommand(Shooter.PRESET_FRONT_PYRAMID));
    addSequential(new WaitCommand(0.25));
    addSequential(new RunIntakeCommand(0.5));
    addSequential(new ShootCommand());
    addSequential(new LoadAndShootCommand());
    addSequential(new ShooterOnCommand(false));
    addSequential(new RunIntakeCommand(0.0));
  }
}
