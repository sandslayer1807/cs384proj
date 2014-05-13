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
    private final int diameter;
    private String cur_loc = "null";
    
    
    public StatePanel() {
        this.state = "null";
        this.finished = false;
        setPreferredSize(new Dimension(900, 100));
        diameter = 100;
        //System.out.println("This.getwidth: "+this.getWidth()+ " and right_loc: "+right_loc);
        this.setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        left_loc = this.getWidth()/8;
        right_loc = 7*(this.getWidth()/8);
        if(state.equalsIgnoreCase("finished")) {
            g.setColor(Color.GREEN);
            finish();
            if(cur_loc.equalsIgnoreCase("left"))
                 g.fillOval(left_loc, this.getHeight()/3, diameter/2, diameter/2);
            else
                g.fillOval(right_loc, this.getHeight()/3, diameter/2, diameter/2);
        }
        else if(state.equalsIgnoreCase("left")) {
            g.setColor(Color.RED);
            g.fillOval(left_loc, this.getHeight()/3, diameter/2, diameter/2);
            cur_loc = "left";
        }
        else if(state.equalsIgnoreCase("right")) {
            g.setColor(Color.BLUE);
            g.fillOval(right_loc, this.getHeight()/3, diameter/2, diameter/2);
            cur_loc = "right";
        }
    }
    
    public void setState(String state) {
        this.state = state;
        repaint();
    }
    
    public String getState() {
        return state;
    }
    
    public void finish() {
        finished = true;
        repaint();
    }
    
    public String returnLoc() {
        return cur_loc;
    }
}
