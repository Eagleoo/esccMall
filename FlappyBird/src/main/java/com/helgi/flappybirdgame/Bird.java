package com.helgi.flappybirdgame;

import android.graphics.Matrix;

import com.helgi.framework.Game;
import com.helgi.framework.Graphics;
import com.helgi.framework.Screen;
import com.helgi.framework.implementation.Animation;

public class Bird {
	  
	Graphics g;
	double res;
	double gameWidth;
	double gameHeight;
	Matrix matrix; // B�um til reglu um hvernig pixlunum er ra�a� � sn�nu myndinni
	public double X;
	public double Y;
	private double rotation;
	private double YSpeed;
	private double rotSpeed;
	private Animation anim;
	
	/* X er [0;800]
	 * Y er [0;1280]
	 * YSpeed [-0.9; 0.6] og er hra�i Flappy � y-stefnu
	 * rotation er � bilinu [-20�;20�] og er stefna Flappy
	 * rotSpeed er s� hra�i sem Flappy er a� sn�ast um
	 * lastUp er t�mi �egar up() var s��ast virkja�
	 */
	
	public Bird (Game game, Screen scr) {
		g = game.getGraphics();
		gameWidth = scr.gameWidth;
		gameHeight = scr.gameHeight;
		res = scr.res;
	    X = gameWidth/2.055;
	    Y = gameHeight/2.33;
	    YSpeed = 0;
	    rotation = 0;
	    rotSpeed = 0;
	    matrix = new Matrix();
	    anim = new Animation();
	    anim.addFrame(Assets.portrait, 40);
		anim.addFrame(Assets.portrait, 40);
		anim.addFrame(Assets.portrait, 40);
	}

	public void update(float deltaTime) {
	    manageAccAndRot(deltaTime); // Hr��un ni�ur � vi� og sn�ningur
	    anim.update(deltaTime);  
	}
	
	public void draw() {
		g.drawImage(anim.getImage(), matrix, X-5, Y, rotation, res, true);
	}
	  
	public void up() {
	    if (Y > gameHeight/20) {
	      YSpeed = gameHeight/60; // Hr��un upp � vi�
	      Y -= gameHeight/200;
	      rotSpeed = 18; 
	  }
	}
	  
	public void manageAccAndRot(float deltaTime) {
		if (YSpeed > -200) YSpeed -= 0.6*deltaTime;// Hr��un ni�ur � vi�, YSpeed
		Y -= YSpeed;
		
		if (rotSpeed > -4) rotSpeed -= 0.24*deltaTime; // H�markshornhra�i r�tts�lissn�nings er |-4|
	    if (-90 < rotation || rotSpeed > 0) rotation += rotSpeed;
	    if (rotation > 45) {rotation = 45; rotSpeed = 0;} // Svo fuglinn fari ekki � yfirsn�ning
	}
	  
	  
	public double getX() {return X;}
	public double getY() {return Y;}
}