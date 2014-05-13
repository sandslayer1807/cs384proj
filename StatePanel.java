/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Ethan
 */
public class StatePanel extends JPanel {
    private boolean finished;
    private String state;
    private int left_loc;
    private int right_loc;
    private int diameter;
    
    public StatePanel() {
        this.state = null;
        this.finished = false;
        setPreferredSize(new Dimension(900, 100));
        left_loc = this.getWidth()/3;
        right_loc = 2*(this.getWidth()/3);
        diameter = 70;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(finished) {
            g.setColor(Color.GREEN);
        }
        else if(state.equalsIgnoreCase("left")) {
            g.setColor(Color.RED);
            g.fillOval(left_loc, this.getHeight()/2, diameter/2, diameter/2);
        }
        else if(state.equalsIgnoreCase("right")) {
            g.setColor(Color.BLUE);
            g.fillOval(right_loc, this.getHeight()/2, diameter/2, diameter/2);
        }
    }
    
    public void setState(String state) {
        this.state = state;
        repaint();
    }
    
    public void finish() {
        finished = true;
        repaint();
    }
}
