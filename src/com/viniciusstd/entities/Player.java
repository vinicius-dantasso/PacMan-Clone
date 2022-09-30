package com.viniciusstd.entities;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import com.viniciusstd.main.Game;
import com.viniciusstd.main.SoundAdvanced;
import com.viniciusstd.world.Camera;
import com.viniciusstd.world.World;

public class Player extends Entity {
	public boolean right,up,left,down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;

	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
	private int ghostModeTime = 0, maxGhostModeTime = 300;
	public BufferedImage[] spriteLeft;
	public BufferedImage[] spriteRight;

	public Player(int x, int y, int width, int height,double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		spriteLeft = new BufferedImage[2];
		for(int i=0; i<2; i++){
			spriteLeft[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
		spriteRight = new BufferedImage[2];
		for(int i=0; i<2; i++){
			spriteRight[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
	}
	
	public void tick() {
		depth = 1;
		
		if(right && World.isFree((int)(x+speed), this.getY())) {
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed), this.getY())) {
			dir = left_dir;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		else if(down && World.isFree(this.getX(), (int)(y+speed))) {
			y+=speed;
		}

		frames++;
		if(frames == maxFrames){
			frames = 0;
			index++;
			if(index > maxIndex){
				index = 0;
			}
		}

		if(Enemy.ghostMode == true){
			ghostModeTime++;
			if(ghostModeTime == maxGhostModeTime){
				Enemy.ghostMode = false;
				ghostModeTime = 0;
			}
		}

		fruitCollected();
		pillCollected();
		enemyDamage();
	}

	public void pillCollected(){
		for(int i=0; i<Game.entities.size(); i++){
			Entity current = Game.entities.get(i);
			if(current instanceof Pill){
				if(Entity.isColidding(this, current)){
					Game.entities.remove(i);
					Game.pillCount++;
					return;
				}
			}
		}
	}

	public void enemyDamage(){
		for(int i=0; i<Game.entities.size(); i++){
			Entity current = Game.entities.get(i);
			if(current instanceof Enemy){
				if(Entity.isColidding(this, current)){
					if(Enemy.ghostMode == true){
						Game.entities.remove(i);
					}
					else{
						SoundAdvanced.bgMusic.stop();
						SoundAdvanced.deathSound.play();
						Game.gameState = "GAME_OVER";
						Game.entities.remove(this);
					}
					return;
				}
			}
		}
	}

	public static void fruitCollected(){
		for(int i=0; i<Game.entities.size(); i++){
			Entity current = Game.entities.get(i);
			if(current instanceof Fruit){
				if(Entity.isColidding(Game.player, current)){
					Game.entities.remove(i);
					Enemy.ghostMode = true;
				}
			}
		}
	}

	public void render(Graphics g){
		if(dir == right_dir){
			g.drawImage(spriteRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		else if(dir == left_dir){
			g.drawImage(spriteLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
	
}
