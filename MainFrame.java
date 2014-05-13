/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author Ethan
 */
public class MainFrame extends JApplet implements ActionListener {
    
    private static final Dimension MASTER_WINDOW_SIZE = new Dimension (1000,700);
    private JInternalFrame authorFrame, problemFrame, algorithmFrame;
    private JRadioButtonMenuItem ricartAgrawala, modRicartAgrawala;
    private JMenuItem authorMenuItem, problemMenuItem;
    private JDesktopPane virtualDesktop;
    private VisualizationPanel vizPanel;
    
    @Override
    public void init() {
        System.out.println("Initializing...");
        initializeContainer(this);
        setSize(MASTER_WINDOW_SIZE);
        setJMenuBar(createJMenu());
    }
    
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex)
        {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JFrame frame = new JFrame("CS384 Project");
        MainFrame m = new MainFrame();
        m.initializeContainer(frame);
        
        frame.setJMenuBar(m.createJMenu());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if(source == authorMenuItem) {
            authorFrame.setVisible(true);
        }
        else if (source == problemMenuItem) {
            problemFrame.setVisible(true);
        }
        
        if(source instanceof JRadioButtonMenuItem) {
            if(ricartAgrawala.isSelected())
                vizPanel.setMethod("ricart-agrawala");
            else if(modRicartAgrawala.isSelected())
                vizPanel.setMethod("modified ricart-agrawala");
            else
                System.out.println("ERROR: Setting method: no recognized method.");
        }
    }
    
    private JMenuBar createJMenu() {
        System.out.println("Creating the JMenu...");
       
        JMenu algMenu = new JMenu("Algorithms");
        ButtonGroup g = new ButtonGroup();
        ricartAgrawala = new JRadioButtonMenuItem("Ricart-Agrawala Algorithm");
        modRicartAgrawala = new JRadioButtonMenuItem("Modified Ricart-Agrawala Algorithm");
        ricartAgrawala.setSelected(true);
        g.add(ricartAgrawala);
        g.add(modRicartAgrawala);
        
        ricartAgrawala.addActionListener(this);
        modRicartAgrawala.addActionListener(this);
        
        algMenu.add(ricartAgrawala);
        algMenu.add(modRicartAgrawala);
        
        JMenu demosMenu = new JMenu("Demos");
        demosMenu.add(algMenu);
        
        authorMenuItem = new JMenuItem("Authors");
        problemMenuItem = new JMenuItem("Problem Description");
        
        authorMenuItem.addActionListener(this);
        problemMenuItem.addActionListener(this);
        
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(authorMenuItem);
        aboutMenu.add(problemMenuItem);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(demosMenu);
        menuBar.add(aboutMenu);
        
        return menuBar;
    }
    
    private void initializeContainer(Container c) {
        System.out.println("Initializing the Container...");
        c.setPreferredSize(MASTER_WINDOW_SIZE);
        
        virtualDesktop = new JDesktopPane();
        virtualDesktop.setPreferredSize(MASTER_WINDOW_SIZE);
        
        algorithmFrame = new JInternalFrame("Algorithm Visualization", true, true, true, true);
        algorithmFrame.setResizable(false);
        algorithmFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        
        vizPanel = new VisualizationPanel();
        algorithmFrame.setContentPane(vizPanel);
        vizPanel.setDisplayedThreads();
        algorithmFrame.pack();
        virtualDesktop.add(algorithmFrame);

        initAboutFrames();
        c.add(virtualDesktop);
        algorithmFrame.setVisible(true);
   
    }
    
    private void initAboutFrames()
    {
        System.out.println("Initializing the about frames...");
        authorFrame = new JInternalFrame("Authors",true,true,true,true);
        problemFrame = new JInternalFrame("Problem Description", true, true, true, true);
        
        final String AUTHOR_TEXT = "Authors:\n Ethan Wells and San Yeung\n";
        JTextArea authorFrameTextArea = new JTextArea(AUTHOR_TEXT);
        authorFrameTextArea.setEditable(false);
        authorFrameTextArea.setLineWrap(true);
        authorFrameTextArea.setColumns(60);
        authorFrame.add(authorFrameTextArea);
        authorFrame.pack();
        
        final String PROBLEM_TEXT = "Two towns A and B are connected with a bridge. Suppose there are four\n" +
                                    "people and their moving directions are indicated by the arrows. The bridge is narrow, and at any\n" +
                                    "time, multiple people cannot pass in the opposite directions.\n" +
                                    "1. Based on the Ricart & Agrawalas mutual exclusion algorithm, design a decentralized protocol\n" +
                                    "so that at most one person can be on the bridge at any given time, and no person is indeﬁnitely\n" +
                                    "prevented from crossing the bridge. Treat each person to be a process, and assume that their\n" +
                                    "clocks are synchronized.\n" +
                                    "2. Design another protocol so that multiple people can be on the bridge as long as they are\n" +
                                    "moving in the same direction, but no person is indeﬁnitely prevented from crossing the bridge.\n" +
                                    "Design a graphical user interface to display the movement of the people, so that the instructor can\n" +
                                    "control the walking speed of the people and verify the protocol. Note that to receive full credits,\n" +
                                    "you should provide instructions on how to compile and run your program, and your program should\n" +
                                    "be well-documented.";
        JTextArea problemFrameTextArea = new JTextArea(PROBLEM_TEXT);
        problemFrameTextArea.setEditable(false);
        problemFrameTextArea.setLineWrap(true);
        problemFrameTextArea.setColumns(70);
        problemFrame.add(problemFrameTextArea);
        problemFrame.pack();
        
        authorFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        problemFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        virtualDesktop.add(authorFrame);
        virtualDesktop.add(problemFrame);
    }
    
}
