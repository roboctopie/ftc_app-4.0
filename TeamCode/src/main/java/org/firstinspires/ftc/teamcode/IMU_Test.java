/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Random;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.abs;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive TeleOp for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="IMU TEST!!", group="Linear Opmode")
//@Disabled
public class IMU_Test extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    BNO055IMU imu;
    DcMotor RightMotor;
    DcMotor LeftMotor;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' mu st correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        RightMotor = hardwareMap.dcMotor.get("motor_right");
        LeftMotor = hardwareMap.dcMotor.get("motor_left");
        RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            Orientation angles = imu.getAngularOrientation();
            if(gamepad1.left_bumper)  Turn(90 , RightMotor, LeftMotor, imu);
            if(gamepad1.right_bumper) Turn(-90, RightMotor, LeftMotor, imu);
            telemetry.addData("IMU Angle 1", angles.firstAngle);
            telemetry.addData("IMU Angle 2", angles.secondAngle);
            telemetry.addData("IMU Angle 3", angles.thirdAngle);
            telemetry.update();
        }
    }
    public void Turn(float degrees, DcMotor right_Motor, DcMotor left_Motor, BNO055IMU imu)
    {
        Orientation angles = imu.getAngularOrientation();
        float degreesToTurn = ((degrees + (angles.firstAngle-180)) % 360);
        float turn; //= degreesToTurn - angles.firstAngle;

        while(angles.firstAngle > degreesToTurn + 5 | angles.firstAngle < degreesToTurn - 5 & opModeIsActive())
        {
            turn         = (degreesToTurn - angles.firstAngle)*8;
            angles = imu.getAngularOrientation();

            if(turn != 0) right_Motor.setPower(-(turn / 360 + ((turn / abs(turn)) * 0.3)));
            if(turn != 0)  left_Motor.setPower(  turn / 360 + ((turn / abs(turn)) * 0.3));

            telemetry.addData("IMU Angle 1",      angles.firstAngle);
            telemetry.addData("IMU Angle 2",      angles.secondAngle);
            telemetry.addData("IMU Angle 3",      angles.thirdAngle);
            telemetry.addData("Degrees Variable", degreesToTurn);
            telemetry.update();
        }
        left_Motor.setPower(0);
        right_Motor.setPower(0);

    }
    public void Forward(float power, DcMotor right_Motor,DcMotor left_Motor,BNO055IMU imu)
    {
        Orientation angles = imu.getAngularOrientation();
        power = power / 100;
        float degrees = angles.firstAngle / 360;
        while(angles.firstAngle > degrees + 3 & angles.firstAngle < degrees + 3 & opModeIsActive())
        {
            angles = imu.getAngularOrientation();
            right_Motor.setPower(power + ((0 - angles.firstAngle) - degrees));
            //left_Motor.setPower(power + ((angles.firstAngle)angles  - degrees));
        }
    }

}
