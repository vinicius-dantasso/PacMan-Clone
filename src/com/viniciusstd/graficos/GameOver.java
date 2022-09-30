package com.viniciusstd.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameOver {

    private boolean showGameOverMsg = true;
    private int framesGameOver = 0;

    public void tick(){
        framesGameOver++;
        if(framesGameOver == 24){
            framesGameOver = 0;
            if(showGameOverMsg){
                showGameOverMsg = false;
            }
            else{
                showGameOverMsg = true;
            }
        }
    }

    public void render(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 150, 200);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if(showGameOverMsg)
            g.drawString(">Pressione Enter Para Recomeçar<", 90, 230);
    }
}
