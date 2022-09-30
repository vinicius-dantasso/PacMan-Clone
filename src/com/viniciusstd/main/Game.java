package com.viniciusstd.main;

import com.viniciusstd.entities.Entity;
import com.viniciusstd.entities.Player;
import com.viniciusstd.graficos.GameOver;
import com.viniciusstd.graficos.SpriteSheet;
import com.viniciusstd.graficos.UI;
import com.viniciusstd.graficos.WinScreen;
import com.viniciusstd.world.World;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static SpriteSheet spritesheet;
	public static Player player;
	public static World world;
	public UI ui;
	public GameOver gameOverScreen;
	public WinScreen win;

	public static int pillCount = 0;
	public static int pillTotal = 0;

	public static String gameState = "NORMAL";
	private boolean gameRestart = false;

	private int curLevel = 1, maxLevel = 2;
	private int loadingTime = 0;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		//Inicializando objetos
		spritesheet = new SpriteSheet("/spritesheet.png");
		player = new Player(0,0,16,16,1, spritesheet.getSprite(32, 0, 16, 16));
		entities = new ArrayList<Entity>();
		world = new World("/level1.png");
		ui = new UI();
		gameOverScreen = new GameOver();
		win = new WinScreen();
		entities.add(player);
	}
	
	public void initFrame() {
		frame = new JFrame("Pac-Man");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game jogo = new Game();
		jogo.start();
	}
	
	public void tick() {
		if(gameState == "NORMAL"){
			SoundAdvanced.bgMusic.loop();
			gameRestart = false;
			for(int i=0; i<entities.size(); i++) {
				Entity e = entities.get(i);
				if(e instanceof Player) {
					//Dando tick no player
				}
				e.tick();
			}

			if(pillCount == pillTotal){
				loadingTime++;
				if(loadingTime == 60){
					curLevel++;
					loadingTime = 0;
					if(curLevel > maxLevel){
						gameState = "YOU_WIN";
						return;
					}
					pillCount = 0;
					pillTotal = 0;
					String newWorld = "level"+curLevel+".png";
					World.restartGame(newWorld);
				}
			}
		}
		else if(gameState == "GAME_OVER"){
			gameOverScreen.tick();
			if(gameRestart){
				gameState = "NORMAL";
				curLevel = 1;
				pillCount = 0;
				pillTotal = 0;
				String newWorld = "level"+curLevel+".png";
				World.restartGame(newWorld);
			}
		}
		else if(gameState == "YOU_WIN"){
			win.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		//RENDERIZAÇÃO DO JOGO
		world.render(g);
		Collections.sort(entities, Entity.depthSorter);
		for(int i=0; i<entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);

		if(gameState == "GAME_OVER"){
			gameOverScreen.render(g);
		}
		else if(gameState == "YOU_WIN"){
			win.render(g);
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}

		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			this.gameRestart = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
