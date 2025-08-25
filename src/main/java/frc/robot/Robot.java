package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

public class Robot extends TimedRobot {  

private final Joystick joystick0 = new Joystick(0);
private final Joystick joystick1 = new Joystick(1);

private final TalonSRX leftFront  = new TalonSRX(21);
private final TalonSRX rightFront = new TalonSRX(22);
private final TalonSRX leftBack   = new TalonSRX(23);
private final TalonSRX rightBack  = new TalonSRX(24);

private final AHRS navx = new AHRS(NavXComType.kMXP_SPI);
private final TimeOfFlight toFSensor = new TimeOfFlight(3);

private double incDecSpeed = 0.4;

public Robot() {

CameraServer.startAutomaticCapture();
toFSensor.setRangingMode(RangingMode.Short, 40);

leftFront.setInverted(false);
rightFront.setInverted(true);
leftBack.setInverted(false);
rightBack.setInverted(true);
leftFront.setNeutralMode(NeutralMode.Brake);
rightFront.setNeutralMode(NeutralMode.Brake);
leftBack.setNeutralMode(NeutralMode.Brake);
rightBack.setNeutralMode(NeutralMode.Brake);
leftFront.configOpenloopRamp(0.5);
rightFront.configOpenloopRamp(0.5);
leftBack.configOpenloopRamp(0.5);
rightBack.configOpenloopRamp(0.5);
leftFront.configPeakCurrentLimit(40);
rightFront.configPeakCurrentLimit(40);
leftBack.configPeakCurrentLimit(40);
rightBack.configPeakCurrentLimit(40);
leftFront.configPeakCurrentDuration(0);
rightFront.configPeakCurrentDuration(0);
leftBack.configPeakCurrentDuration(0);
rightBack.configPeakCurrentDuration(0);
leftFront.configContinuousCurrentLimit(40);
rightFront.configContinuousCurrentLimit(40);
leftBack.configContinuousCurrentLimit(40);
rightBack.configContinuousCurrentLimit(40);
leftFront.configVoltageCompSaturation(12);
rightFront.configVoltageCompSaturation(12);
leftBack.configVoltageCompSaturation(12);
rightBack.configVoltageCompSaturation(12);
leftFront.enableCurrentLimit(true);
rightFront.enableCurrentLimit(true);
leftBack.enableCurrentLimit(true);
rightBack.enableCurrentLimit(true);
leftFront.enableVoltageCompensation(true);
rightFront.enableVoltageCompensation(true);
leftBack.enableVoltageCompensation(true);
rightBack.enableVoltageCompensation(true);
}

@Override
public void robotPeriodic() {

// SmartDashboard Setup
SmartDashboard.putNumber("Joystick X-Axis: ",   joystick0.getRawAxis(0));
SmartDashboard.putNumber("Joystick0 Y-Axis: ",   -joystick0.getRawAxis(1));
SmartDashboard.putNumber("Left Front Motor: ",   leftFront.getSelectedSensorPosition());
SmartDashboard.putNumber("Left Back Motor: ",   leftBack.getSelectedSensorPosition());
SmartDashboard.putNumber("Right Front Motor: ",   rightFront.getSelectedSensorPosition());
SmartDashboard.putNumber("Right Back Motor: ",   rightBack.getSelectedSensorPosition());
SmartDashboard.putNumber("navX Gyro YAW: ",   navx.getYaw());
SmartDashboard.putNumber("Laser Time of Flight: ",   toFSensor.getRange());

// Reset Drivetrain Encoders
String DRIVETRAIN_RESET = "J1 B3";
SmartDashboard.putString("Drivetrain Encoder Reset Button: ", DRIVETRAIN_RESET);
if (joystick1.getRawButtonReleased(3)) {
    leftFront.setSelectedSensorPosition(0);
    leftBack.setSelectedSensorPosition(0); 
    rightFront.setSelectedSensorPosition(0);
    rightBack.setSelectedSensorPosition(0); }    

// Reset navX Encoders
String NAVX_RESET = "J0 B2";
SmartDashboard.putString("navX Gyro Reset Button: ",   NAVX_RESET);
if (joystick0.getRawButtonReleased(2)) {
    navx.reset(); }
 }

@Override
public void autonomousInit() {}
@Override
public void autonomousPeriodic() {}

@Override
public void teleopInit() {
// Initiates that all mechanisms are off
// Initiates all plugged-in Talon SRX Mag Encoders
leftFront.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
leftFront.setSensorPhase(true);
leftFront.setInverted(false);
rightFront.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
rightFront.setSensorPhase(true);
rightFront.setInverted(true);
leftBack.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
leftBack.setSensorPhase(true);
leftBack.setInverted(false);
rightBack.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
rightBack.setSensorPhase(true);
rightBack.setInverted(true);
}

@Override
public void teleopPeriodic() {

// Drivetrain Joystick Deadband
double turnDeadband = joystick0.getRawAxis(0); // X Axis
double turn;
if( Math.abs(turnDeadband) < 0.15 ) {
  turn = 0; }
else {
  turn = turnDeadband; }

double driveDeadband = joystick0.getRawAxis(1); // Y Axis
double drive = 0;
if( Math.abs(driveDeadband) < 0.15 ) {
  drive = 0; }
else {
  drive = driveDeadband; }    

// Robot Driving Control and Speed
if(joystick0.getRawButtonPressed(5)) {
  incDecSpeed = 0.4; }  
if(joystick0.getRawButtonPressed(6)) {
  incDecSpeed = 0.85; } 
String SLOW_FAST_TOGGLE = "J0 B5-B6";
SmartDashboard.putString("Drivetrain Speed (Slow - Fast):",   SLOW_FAST_TOGGLE);

leftFront.set(ControlMode.PercentOutput,  ((drive - turn) * incDecSpeed));
leftBack.set(ControlMode.PercentOutput,   ((drive - turn) * incDecSpeed));
rightFront.set(ControlMode.PercentOutput, ((drive + turn) * incDecSpeed));
rightBack.set(ControlMode.PercentOutput,  ((drive + turn) * incDecSpeed));
}

@Override
public void disabledInit() {}
@Override
public void disabledPeriodic() {}
@Override
public void testInit() {}
@Override
public void testPeriodic() {}
@Override
public void simulationInit() {}
@Override
 public void simulationPeriodic() {}
}
