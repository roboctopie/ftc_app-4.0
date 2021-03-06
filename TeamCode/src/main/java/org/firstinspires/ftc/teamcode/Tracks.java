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

import android.sax.TextElementListener;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Random;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Tex`leop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Tracks", group="Linear Opmode")
//@Disabled
public class  Tracks extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor RightMotor;
    DcMotor LeftMotor;
    DcMotor Arm;
    Servo Basket;
    DcMotor Collector1;
    DcMotor Collector2;
    float basketPos = 180;
    Random rand = new Random();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        RightMotor = hardwareMap.dcMotor.get("motor_right");
        LeftMotor = hardwareMap.dcMotor.get("motor_left");
        Arm = hardwareMap.dcMotor.get("arm");
        Basket = hardwareMap.servo.get("basket");
        Collector1 = hardwareMap.dcMotor.get("collector1");
        Collector2 = hardwareMap.dcMotor.get("collector2");


        // Most robots need the motor on one side to be reversed to drive forward
        RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.addData("License", "Read/Write/Run");
            if(gamepad1.right_stick_x==0)
            {
                LeftMotor.setPower(gamepad1.left_stick_y);
                telemetry.addData("Left Motor Power", gamepad1.left_stick_y);
                RightMotor.setPower(gamepad1.left_stick_y);
                telemetry.addData("Right Motor Power", gamepad1.left_stick_y);
            }
            else if(gamepad1.right_stick_x>0)
            {
                LeftMotor.setPower(-gamepad1.right_stick_x);
                telemetry.addData("Left Motor Power", -gamepad1.right_stick_x);
                RightMotor.setPower(gamepad1.right_stick_x);
                telemetry.addData("Right Motor Power", gamepad1.right_stick_x);
            }
            else if(gamepad1.right_stick_x<0)
            {
                LeftMotor.setPower(-gamepad1.right_stick_x);
                telemetry.addData("Left Motor Power", -gamepad1.right_stick_x);
                RightMotor.setPower(gamepad1.right_stick_x);
                telemetry.addData("Right Motor Power", gamepad1.right_stick_x);
            }
            if(gamepad1.right_trigger>0)
            {
                Arm.setPower(gamepad1.right_trigger);
                telemetry.addData("Arm Power", gamepad1.right_trigger);
            }
            else if(gamepad1.left_trigger>0)
            {
                Arm.setPower(-gamepad1.left_trigger);
                telemetry.addData("Arm Power", -gamepad1.left_trigger);
            }
            else
            {
                Arm.setPower(0);
                telemetry.addData("Arm Power", "0.0");
            }
            if(gamepad2.y)
            {
                basketPos=50;
            }
            if(gamepad2.x)
            {
                basketPos=180;
            }
            if(gamepad2.b)
            {
                basketPos=160;
            }
            Collector1.setPower(gamepad2.right_stick_y*0.75);
            telemetry.addData("Collector Motor Power", gamepad2.right_stick_y*0.75);
            Collector2.setPower(gamepad2.left_stick_y/4);
            telemetry.addData("Collector Brushes Power", gamepad2.left_stick_y/2);
            Basket.setPosition(basketPos/180);
            telemetry.addData("Left Motor Position", LeftMotor.getCurrentPosition());
            telemetry.addData("Right Motor Position", RightMotor.getCurrentPosition());
            telemetry.addData("Arm Position", Arm.getCurrentPosition());
            telemetry.addData("Collector Motor Position", Collector1.getCurrentPosition());
            telemetry.addData("Collector Brushes Position", Collector2.getCurrentPosition());
            telemetry.addData("Basket Position", basketPos/180);
            telemetry.addData("Gamepad 1 Type", gamepad1.type());
            telemetry.addData("Gamepad 1 LB Position", gamepad1.left_bumper);
            telemetry.addData("Gamepad 1 LT Position", gamepad1.left_trigger);
            telemetry.addData("Gamepad 1 RT Position", gamepad1.right_trigger);
            telemetry.addData("Gamepad 1 Left Stick Y Position", gamepad1.left_stick_y);
            telemetry.addData("Gamepad 1 Right Stick X Position", gamepad1.right_stick_x);
            telemetry.addData("Gamepad 2 Type", gamepad2.type());
            telemetry.addData("Gamepad 2 B Pressed", gamepad2.b);
            telemetry.addData("Gamepad 2 X Pressed", gamepad2.x);
            telemetry.addData("Gamepad 2 Y Pressed", gamepad2.y);
            telemetry.addData("Gamepad 2 Left Stick Y Position", gamepad2.left_stick_y);
            telemetry.addData("Gamepad 2 Right Stick Y Position", gamepad2.right_stick_y);
            telemetry.addData("Randomness", (rand.nextInt(420) + 1)/10);
            telemetry.update();
        }
    }
}
