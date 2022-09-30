package com.viniciusstd.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.viniciusstd.entities.Enemy;
import com.viniciusstd.entities.Entity;
import com.viniciusstd.entities.Fruit;
import com.viniciusstd.entities.Pill;
import com.viniciusstd.entities.Player;
import com.viniciusstd.graficos.SpriteSheet;
import com.viniciusstd.main.Game;

public class World {
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT; 
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx=0; xx<map.getWidth(); xx++) {
				
				for(int yy=0; yy<map.getHeight(); yy++) {
					
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					
					if(pixelAtual == 0xFF000000) {
						//FLOOR
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					else if(pixelAtual == 0xFFFFFFFF) {
						//WALL
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}
					else if(pixelAtual == 0xFF0026FF) {
						//PLAYER
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if(pixelAtual == 0xFF18D3F4) {
						//Instanciar inimigos e adicionar a lista de entidades
						Enemy enemy = new Enemy(xx*16, yy*16, 16, 16, 0.5, null);
						Game.entities.add(enemy);
					}
					else if(pixelAtual == 0xFFD2EFF7){
						//Comprimidos que o pacman coleta
						Pill pill = new Pill(xx*16, yy*16, 16, 16, 0, Entity.pillSprite);
						Game.entities.add(pill);
						Game.pillTotal++;
					}
					else if(pixelAtual == 0xFFC60000){
						Fruit fruit = new Fruit(xx*16, yy*16, 16, 16, 0, Entity.fruitSprite);
						Game.entities.add(fruit);
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext/TILE_SIZE;
		int y1 = yNext/TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1)/TILE_SIZE;
		int y2 = yNext/TILE_SIZE;
		
		int x3 = xNext/TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1)/TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1)/TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1)/TILE_SIZE;
		
		return !(tiles[x1 + (y1*World.WIDTH)] instanceof WallTile ||
				tiles[x2 + (y2*World.WIDTH)] instanceof WallTile ||
				tiles[x3 + (y3*World.WIDTH)] instanceof WallTile ||
				tiles[x4 + (y4*World.WIDTH)] instanceof WallTile);
	}
	
	public static boolean isFreeDynamic(int xNext, int yNext, int width, int height) {
		int x1 = xNext/TILE_SIZE;
		int y1 = yNext/TILE_SIZE;
		
		int x2 = (xNext + width - 1)/TILE_SIZE;
		int y2 = yNext/TILE_SIZE;
		
		int x3 = xNext/TILE_SIZE;
		int y3 = (yNext + height - 1)/TILE_SIZE;
		
		int x4 = (xNext + width - 1)/TILE_SIZE;
		int y4 = (yNext + height - 1)/TILE_SIZE;
		
		return !(tiles[x1 + (y1*World.WIDTH)] instanceof WallTile ||
				tiles[x2 + (y2*World.WIDTH)] instanceof WallTile ||
				tiles[x3 + (y3*World.WIDTH)] instanceof WallTile ||
				tiles[x4 + (y4*World.WIDTH)] instanceof WallTile);
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.entities = new ArrayList<Entity>();
		Game.spritesheet = new SpriteSheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16, 1,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int xx=xStart; xx<=xFinal; xx++) {
			for(int yy=yStart; yy<=yFinal; yy++) {
				if(xx < 0 || yy < 0 || xx>=WIDTH || yy>=HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
