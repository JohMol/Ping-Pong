package main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

	static final int GAME_WIDTH = 1600;
	static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 175;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Ball ball2;
	Score score;
	
	
	GamePanel() {
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH, GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public Ball createBall() {
		return new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
	}
	
	public void newBall() {
		random = new Random();
		ball = createBall();
		ball2 = createBall();
	}
	
	public void newPaddles() {
		paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
	}
	
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}
	
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		ball2.draw(g);
		score.draw(g);
	}
	
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
		ball2.move();
	}
	
	public void checkCollisionBall(Ball b) {
		// bounce ball off top & bottom window edges
				if (b.y <= 0)
					b.setYDirection(-b.yVelocity);
				
				if (b.y >= (GAME_HEIGHT - BALL_DIAMETER))
					b.setYDirection(-b.yVelocity);
				
				// bounces ball off paddles
				if (b.intersects(paddle1)) {
					b.xVelocity = Math.abs(b.xVelocity);
					b.xVelocity++;
					
					if (b.yVelocity > 0)
						b.yVelocity++;
					else
						b.yVelocity--;
					
					b.setXDirection(b.xVelocity);
					b.setYDirection(b.yVelocity);
				}
				
				if (b.intersects(paddle2)) {
					b.xVelocity = Math.abs(b.xVelocity);
					b.xVelocity++;
					
					if (b.yVelocity > 0)
						b.yVelocity++;
					else
						b.yVelocity--;
					
					b.setXDirection(-b.xVelocity);
					b.setYDirection(b.yVelocity);
				}
	}
	
	public void checkCollision() {
		checkCollisionBall(ball);
		checkCollisionBall(ball2);
		// stops paddles at window edges
		if (paddle1.y <= 0)
			paddle1.y = 0;
		
		if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
			paddle1.y = (GAME_HEIGHT - PADDLE_HEIGHT);
		
		if (paddle2.y <= 0)
			paddle2.y = 0;
		
		if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
			paddle2.y = (GAME_HEIGHT - PADDLE_HEIGHT);
		
		if (ball.x <= 0 || ball2.x <= 0) {
			score.player2++;
			newPaddles();
			newBall();
		}
		
		if (ball.x >= GAME_WIDTH - BALL_DIAMETER || ball2.x >= GAME_WIDTH - BALL_DIAMETER) {
			score.player1++;
			newPaddles();
			newBall();
		}
	}
	
	public void run() {
		// game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	
	public class AL extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
		
	}
	
}
