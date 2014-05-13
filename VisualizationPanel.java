/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Ethan
 */
public class VisualizationPanel extends JPanel implements ActionListener, ChangeListener {

    private final JSlider delaySlider;
    private final JButton startButton, resetButton;
    private final JPanel algPanel;
    private final List<WorkingThread> threads;
    private final TitledBorder delaySliderBorder;
    private int method = 0;
    
    private int[][] responseArray = new int[4][4];
    private int[][] requestArray = new int[4][4];
    
    public VisualizationPanel() {
        System.out.println("Making the Viz Panel...");
        threads = new LinkedList<WorkingThread>();
        delaySlider = new JSlider(100, 600, 200);
        delaySlider.addChangeListener(this);
        
        delaySliderBorder = new TitledBorder(delaySlider.getValue() + " ms");
        
        delaySlider.setBorder(delaySliderBorder);
        
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        
        JToolBar buttonsBar = new JToolBar();
        buttonsBar.add(startButton);
        buttonsBar.add(resetButton);
        
        algPanel = new JPanel();
        algPanel.setLayout(new GridLayout(0,1));
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 3;
        add(delaySlider, c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        add(buttonsBar, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 100;
        c.gridwidth = 8;
        add(new JScrollPane(algPanel), c);
    }
    
    public void setDisplayedThreads() {
        System.out.println("Setting the displayed threads...");
        for(WorkingThread w: threads) {
                w.setWorking(false);
            }
        algPanel.removeAll();
        threads.clear();
        for(int i = 0; i < 4; i++)
        {
            WorkingThread p = new WorkingThread(new StatePanel(),i);
            p.setMethod(this.method);
            threads.add(p);
            algPanel.add(p.displayPanel);
            p.displayPanel.setBorder(new TitledBorder("Thread "+p.getId()));
            p.displayPanel.setVisible(true);
        }
        
        for (WorkingThread p : threads) {
            p.setDelay(delaySlider.getValue());
        }
        //System.out.println("Size: "+algPanel.size());
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton) {
            System.out.println("Started Working");
            for(int i = 0; i < 4; i++) {
                if( i % 2 == 0)
                    threads.get(i).setState("right");
                else
                    threads.get(i).setState("left");
            }
            for(WorkingThread w: threads) {
                w.setCoworkers(threads);
                w.setWorking(true);
                w.execute();
            }
        } else if (e.getSource() == resetButton)
        {
            System.out.println("Stopped Working");
            for(WorkingThread w: threads) {
                w.setWorking(false);
            }
            setDisplayedThreads();
        }
    }

    public void stateChanged(ChangeEvent e) {
        delaySliderBorder.setTitle(delaySlider.getValue() + " ms");
        if (delaySlider.getValueIsAdjusting())
            return;
        int newDelay = delaySlider.getValue();
        
        for(WorkingThread w : threads) {
            w.setDelay(newDelay);
        }
    }
    
    public void setMethod(String method) {
        setDisplayedThreads();
        System.out.println("Setting new method.");
        if(method.equalsIgnoreCase("ricart-agrawala"))
            this.method = 0;
        else if(method.equalsIgnoreCase("modified ricart-agrawala"))
            this.method = 1;
        
        for (WorkingThread i : threads) {
            i.setMethod(this.method);
        }
    }
    
}
