package com.viniciusstd.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.viniciusstd.main.Game;
import com.viniciusstd.world.AStar;
import com.viniciusstd.world.AStarRunAway;
import com.viniciusstd.world.Camera;
import com.viniciusstd.world.Vector2i;

public class Enemy extends Entity {

	public static BufferedImage enemySprite = Game.spritesheet.getSprite(64, 0, 16, 16);
	public static BufferedImage enemyGhostSprite = Game.spritesheet.getSprite(80, 0, 16, 16);

	public static boolean ghostMode = false;

	public Enemy(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, null);
	}
	
	public void tick() {
		if(ghostMode == false){
			if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
				path = AStar.findPath(Game.world, start, end);
			}
			
			if(new Random().nextInt(100) < 75)
				followPath(path);
			
			if(x % 16 == 0 && y % 16 == 0){
				if(new Random().nextInt(100) < 10){
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
					path = AStar.findPath(Game.world, start, end);
				}
			}
		}
		else{
			path = null;
			if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
				Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
				path = AStarRunAway.findPath(Game.world, start, end);
			}
			
			if(new Random().nextInt(100) < 75)
				followPath(path);
			
			if(x % 16 == 0 && y % 16 == 0){
				if(new Random().nextInt(100) < 10){
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
					path = AStarRunAway.findPath(Game.world, start, end);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(ghostMode == false){
			g.drawImage(enemySprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		else{
			g.drawImage(enemyGhostSprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
	
}
