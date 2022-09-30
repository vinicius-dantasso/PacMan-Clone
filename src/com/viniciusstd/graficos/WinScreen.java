package com.viniciusstd.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class WinScreen {

    private boolean showMessage = true;
    private int frames = 0;

    public void tick(){
        frames++;
        if(frames == 24){
            frames = 0;
            if(showMessage){
                showMessage = false;
            }
            else{
                showMessage = true;
            }
        }
    }

    public void render(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        if(showMessage)
            g.drawString("You Win!", 180, 200);
    }

}
