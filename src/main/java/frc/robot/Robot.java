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

private final Joystick m_joystick0 = new Joystick(0);
private final Joystick m_joystick1 = new Joystick(1);

private final TalonSRX m_leftFront  = new TalonSRX(21);
private final TalonSRX m_rightFront = new TalonSRX(22);
private final TalonSRX m_leftBack   = new TalonSRX(23);
private final TalonSRX m_rightBack  = new TalonSRX(24);

private final AHRS m_navx = new AHRS(NavXComType.kMXP_SPI);
private final TimeOfFlight m_toFSensor = new TimeOfFlight(3);

private double incDecSpeed = 0.4;

public Robot() {

CameraServer.startAutomaticCapture();
m_toFSensor.setRangingMode(RangingMode.Short, 40);

m_leftFront.setInverted(false);
m_rightFront.setInverted(true);
m_leftBack.setInverted(false);
m_rightBack.setInverted(true);
m_leftFront.setNeutralMode(NeutralMode.Brake);
m_rightFront.setNeutralMode(NeutralMode.Brake);
m_leftBack.setNeutralMode(NeutralMode.Brake);
m_rightBack.setNeutralMode(NeutralMode.Brake);
m_leftFront.configOpenloopRamp(0.5);
m_rightFront.configOpenloopRamp(0.5);
m_leftBack.configOpenloopRamp(0.5);
m_rightBack.configOpenloopRamp(0.5);
m_leftFront.configPeakCurrentLimit(40);
m_rightFront.configPeakCurrentLimit(40);
m_leftBack.configPeakCurrentLimit(40);
m_rightBack.configPeakCurrentLimit(40);
m_leftFront.configPeakCurrentDuration(0);
m_rightFront.configPeakCurrentDuration(0);
m_leftBack.configPeakCurrentDuration(0);
m_rightBack.configPeakCurrentDuration(0);
m_leftFront.configContinuousCurrentLimit(40);
m_rightFront.configContinuousCurrentLimit(40);
m_leftBack.configContinuousCurrentLimit(40);
m_rightBack.configContinuousCurrentLimit(40);
m_leftFront.configVoltageCompSaturation(12);
m_rightFront.configVoltageCompSaturation(12);
m_leftBack.configVoltageCompSaturation(12);
m_rightBack.configVoltageCompSaturation(12);
m_leftFront.enableCurrentLimit(true);
m_rightFront.enableCurrentLimit(true);
m_leftBack.enableCurrentLimit(true);
m_rightBack.enableCurrentLimit(true);
m_leftFront.enableVoltageCompensation(true);
m_rightFront.enableVoltageCompensation(true);
m_leftBack.enableVoltageCompensation(true);
m_rightBack.enableVoltageCompensation(true);
}

@Override
public void robotPeriodic() {

// SmartDashBoard Set-Up
SmartDashboard.putNumber("Joystick X-Axis: ",   m_joystick0.getRawAxis(0));
SmartDashboard.putNumber("Joystick0 Y-Axis: ",   -m_joystick0.getRawAxis(1));
SmartDashboard.putNumber("Left Front Motor: ",   m_leftFront.getSelectedSensorPosition());
SmartDashboard.putNumber("Left Back Motor: ",   m_leftBack.getSelectedSensorPosition());
SmartDashboard.putNumber("Right Front Motor: ",   m_rightFront.getSelectedSensorPosition());
SmartDashboard.putNumber("Right Back Motor: ",   m_rightBack.getSelectedSensorPosition());
SmartDashboard.putNumber("navX Gyro YAW: ",   m_navx.getYaw());
SmartDashboard.putNumber("Laser Time of Flight: ",   m_toFSensor.getRange());

// Reset Drivetrain Encoders
String Drivetrain_Reset = "J1 B3";
SmartDashboard.putString("Drivetrain Encoder Reset Button: ", Drivetrain_Reset);
if (m_joystick1.getRawButtonReleased(3)) {
    m_leftFront.setSelectedSensorPosition(0);
    m_leftBack.setSelectedSensorPosition(0); 
    m_rightFront.setSelectedSensorPosition(0);
    m_rightBack.setSelectedSensorPosition(0); }    

// Reset navX Encoders
String navX_Reset = "J0 B2";
SmartDashboard.putString("navX Gyro Reset Button: ",   navX_Reset);
if (m_joystick0.getRawButtonReleased(2)) {
    m_navx.reset(); }
 }

@Override
public void autonomousInit() {}
@Override
public void autonomousPeriodic() {}

@Override
public void teleopInit() {
// Initiates that all Mechanisms are Off
// Initiates all Pluged-in Talon SRX Mag Encoders
m_leftFront.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
m_leftFront.setSensorPhase(true);
m_leftFront.setInverted(false);
m_rightFront.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
m_rightFront.setSensorPhase(true);
m_rightFront.setInverted(true);
m_leftBack.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
m_leftBack.setSensorPhase(true);
m_leftBack.setInverted(false);
m_rightBack.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 50);
m_rightBack.setSensorPhase(true);
m_rightBack.setInverted(true);
}

@Override
public void teleopPeriodic() {
   
// Drivetrain - Joystick Deadband
double turnDeadband = m_joystick0.getRawAxis(0); // X Axis
double turn;
if( Math.abs(turnDeadband) < 0.15 ) {
  turn = 0; }
else {
  turn = turnDeadband; }

double driveDeadband = m_joystick0.getRawAxis(1); // Y Axis
double drive = 0;
if( Math.abs(driveDeadband) < 0.15 ) {
  drive = 0; }
else {
  drive = driveDeadband; }    

// Robot Driving Control and Speed
if(m_joystick0.getRawButtonPressed(5)) {
  incDecSpeed = 0.4; }  
if(m_joystick0.getRawButtonPressed(6)) {
  incDecSpeed = 0.85; } 
String Slow_Fast = " J0 5 _ 6";
SmartDashboard.putString("Drivetrain Speed  Slow _ Fast  ",   Slow_Fast);

m_leftFront.set(ControlMode.PercentOutput,  ((drive - turn) * incDecSpeed));
m_leftBack.set(ControlMode.PercentOutput,   ((drive - turn) * incDecSpeed));
m_rightFront.set(ControlMode.PercentOutput, ((drive + turn) * incDecSpeed));
m_rightBack.set(ControlMode.PercentOutput,  ((drive + turn) * incDecSpeed));
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
