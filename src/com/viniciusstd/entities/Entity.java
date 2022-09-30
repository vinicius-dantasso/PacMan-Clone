package com.viniciusstd.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import com.viniciusstd.main.Game;
import com.viniciusstd.world.Camera;
import com.viniciusstd.world.Node;
import com.viniciusstd.world.Vector2i;
import com.viniciusstd.world.World;

public class Entity {
	
	protected double x, y;
	protected int width, height;
	private BufferedImage sprite;
	
	protected List<Node> path;
	protected double speed;
	
	public int depth;

	public static Random rand = new Random();

	public static BufferedImage pillSprite = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage fruitSprite = Game.spritesheet.getSprite(16, 16, 16, 16);
	
	public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.speed = speed;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}
	
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public void tick() {}
	
	//PROFUNDIDADE DE RENDERIZAÇÃO
	public static Comparator<Entity> depthSorter = new Comparator<Entity>() {
			
			@Override
			public int compare(Entity n0, Entity n1) {
				if(n1.depth < n0.depth)
					return +1;
				if(n1.depth > n0.depth)
					return -1;
				return 0;
			}
	};
	
	//Calcula Distância entre entidades
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				if(x < target.x * 16) {
					x+=speed;
				}
				else if(x > target.x * 16) {
					x-=speed;
				}
				
				if(y < target.y * 16) {
					y+=speed;
				}
				else if(y > target.y * 16) {
					y-=speed;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size()-1);
				}
			}
		}
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		/*
		g.setColor(Color.red);
		g.fillRect(this.getX() + 5 - Camera.x, this.getY() + 5 - Camera.y, this.getWidth() - 8, this.getHeight() - 8);
		*/
	}
}
