// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //------------------Motor Declarations-------------------------------//

  WPI_VictorSPX _Drive_Left_Master =  new  WPI_VictorSPX(Constants.DRIVE_MOTOR_LEFT_MASTER);
  WPI_VictorSPX _Drive_Left_Follower =  new  WPI_VictorSPX(Constants.DRIVE_MOTOR_LEFT_FOLLOWER);
  WPI_VictorSPX _Drive_Right_Master =  new  WPI_VictorSPX(Constants.DRIVE_MOTOR_RIGHT_MASTER);
  WPI_VictorSPX _Drive_Right_Follower =  new  WPI_VictorSPX(Constants.DRIVE_MOTOR_RIGHT_FOLLOWER);
  TalonSRX _Shooter =  new  TalonSRX(Constants.SHOOT_MOTOR);
  TalonSRX _Intake =  new  TalonSRX(Constants.INTAKE_MOTOR);
  Spark _Hang_1 = new Spark(Constants.HANG_MOTOR_1);
  Spark _Hang_2 = new Spark(Constants.HANG_MOTOR_2);



  //--------------------------Controllers-----------------------------------//

  XboxController _xboxDriver = new XboxController(0);
	XboxController _xboxOp = new XboxController(1);

  //-------------------------Needed Varaibles-------------------------//
  
  double hanger = 0;
	double forward = 0;
	double turn = 0;
  Boolean Shoot = false;
  Boolean Eat = false;
  Boolean Spit = false;
  Boolean SlowBot = false;

  DifferentialDrive Cdrive = new DifferentialDrive(_Drive_Left_Master,_Drive_Right_Master);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    /* Ensure motor output is neutral during init */
    _Drive_Left_Master.set(ControlMode.PercentOutput, 0);
    _Drive_Left_Follower.set(ControlMode.PercentOutput, 0);
    _Drive_Right_Master.set(ControlMode.PercentOutput, 0);
    _Drive_Right_Follower.set(ControlMode.PercentOutput, 0);
    _Shooter.set(ControlMode.PercentOutput, 0);
    _Intake.set(ControlMode.PercentOutput, 0);
    _Hang_1.set(0);
    _Hang_2.set(0);

    _Drive_Left_Master.setInverted(false);
    _Drive_Left_Follower.setInverted(false);
    _Drive_Right_Master.setInverted(false);
    _Drive_Right_Follower.setInverted(false);
    _Shooter.setInverted(false);
    _Intake.setInverted(false);
    _Hang_1.setInverted(false);
    _Hang_2.setInverted(false);

    _Drive_Left_Follower.follow(_Drive_Left_Master);
    _Drive_Right_Follower.follow(_Drive_Right_Master);

    _Shooter.setNeutralMode(NeutralMode.Coast);
    _Intake.setNeutralMode(NeutralMode.Coast);
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() 
  {
    if(Shoot)
    {
      _Shooter.set(ControlMode.PercentOutput, Constants.SHOOTER_SPEED);
    }
    else
    {
      _Shooter.set(ControlMode.PercentOutput, 0);
    }

    if(Eat)
    {
      _Intake.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
    }
    else if(Spit)
    {
      _Intake.set(ControlMode.PercentOutput, -Constants.INTAKE_SPEED);
    }
    else
    {
      _Intake.set(ControlMode.PercentOutput, 0);
    }

    _Hang_1.set(hanger);
    _Hang_2.set(hanger);
    if(forward<0.2 && forward>-0.2)
    {
	  	SlowBot = true;
    }
    else
	  {
  		SlowBot = false;
  	}
    
    Cdrive.curvatureDrive(forward, -turn*0.4, SlowBot);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
		forward = -1 *_xboxDriver.getLeftY();
		turn = -_xboxDriver.getRightX();
		forward = Deadband(forward);		
		turn = Deadband(turn);
		turn = turn*turn*turn;
		turn = turn *0.8;
		forward = forward*forward*forward;

    if(_xboxDriver.getLeftTriggerAxis() > 0.8) //Apply brakes when left Trigger is held
		{
			_Drive_Left_Master.setNeutralMode(NeutralMode.Brake);
			_Drive_Right_Master.setNeutralMode(NeutralMode.Brake);
			_Drive_Left_Follower.setNeutralMode(NeutralMode.Brake);
			_Drive_Right_Follower.setNeutralMode(NeutralMode.Brake);
		}
		else
		{
			_Drive_Left_Master.setNeutralMode(NeutralMode.Coast);
			_Drive_Right_Master.setNeutralMode(NeutralMode.Coast);
			_Drive_Left_Follower.setNeutralMode(NeutralMode.Coast);
			_Drive_Right_Follower.setNeutralMode(NeutralMode.Coast);

		}
		if(_xboxDriver.getRightTriggerAxis() < 0.8)  //Go faster when Right Trigger held
		{
			forward = forward * Constants.DRIVE_BASE_SPEED;
		}

    if(_xboxOp.getRightTriggerAxis() < 0.8)  //Shoot
    {
      Shoot = true;
    }
    else
    {
      Shoot = false;
    }
    if(_xboxOp.getLeftTriggerAxis() > 0.8)
    {
      Spit = false;
      Eat = true;
    }
    else if(_xboxOp.getLeftBumper())
    {
      Spit = true;
      Eat = false;
    }
    else
    {
      Spit = false;
      Eat = false;
    }
    hanger = _xboxOp.getRightY();

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}

  double Deadband(final double value) 
	{
		/* Upper deadband */
		if (value >= 0.01 || value <= -0.01) 
			return value;

		/* Outside deadband */
		return 0;
	}
}
