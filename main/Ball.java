package main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle {

	Random random;
	int xVelocity;
	int yVelocity;
	int initialSpeed = 4;
	
	Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		random = new Random();
		int randomX = random.nextInt(2);
		if (randomX == 0)
			randomX--;
		setXDirection(randomX * initialSpeed);
		
		int randomY = random.nextInt(2);
		if (randomY == 0)
			randomY--;
		setYDirection(randomY * initialSpeed);
	}
	
	public void setXDirection(int x) {
		xVelocity = x;
	}
	
	public void setYDirection(int y) {
		yVelocity = y;
	}
	
	public void move() {
		x += xVelocity;
		y += yVelocity;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}
	
}
