/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

import javax.swing.SwingWorker;

/**
 *
 * @author Ethan
 */
public class WorkingThread extends SwingWorker<Void, Void> {
    protected final StatePanel displayPanel;
    private volatile long delay;
    private int id;
    private int method;
    private volatile boolean stillWorking = false;
    
    public WorkingThread(StatePanel displayPanel, int id) {
        this.displayPanel = displayPanel;
        this.delay = 0;
        this.id = id;
        this.method = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public long getDelay() {
        return delay;
    }
    
    public void setDelay(long delay) {
        this.delay = delay;
    }
    
    public void setMethod(int method)
    {
        this.method = method;
    }
    
    public void setWorking(boolean value) {
        this.stillWorking = value;
    }
    
    protected void timedPublish() {
        try {
            if(delay > 0)
            {
                Thread.sleep(delay);
            }
        } catch (InterruptedException ex) {
            System.err.println("Unexpectedly exited sleep in worker thread.");
        }
        publish(); // needs work
    }
    
    //needs work

    @Override
    protected Void doInBackground() throws Exception {
        if(method == 0)
            ricartAgrawala();
        else if(method == 1)
            modRicartAgrawala();
        return null;
    }
    
    private void ricartAgrawala() {
        do {
            
        } while(stillWorking);
    }
    
    private void modRicartAgrawala() {
        do {
            
        } while(stillWorking);
    }
}
