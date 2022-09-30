package com.viniciusstd.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.viniciusstd.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 18));
		g.drawString("Points: " +Game.pillCount+"/"+Game.pillTotal, 5, 16);
	}
	
}
