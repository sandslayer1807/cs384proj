/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private volatile List<CriticalRequest> reqs = new LinkedList<CriticalRequest>();
    private CriticalRequest latestRequest;
    private List<WorkingThread> coworkers;
    private volatile int ackCount = 0;
    private static final Object ackLock = new Object();
    private static final Object addLock = new Object();
    private static final Object critLock = new Object();
    
    public WorkingThread(StatePanel displayPanel, int id) {
        this.displayPanel = displayPanel;
        this.delay = 0;
        this.id = id;
        this.method = 0;
        latestRequest = null;
    }
    
    public void setCoworkers(List<WorkingThread> coworkers) {
        this.coworkers = coworkers;
    }
    
    public void addRequest(CriticalRequest thing) {
        synchronized(addLock)
        {
            reqs.add(thing);
            System.out.println("Added: Size of reqs for "+id+" is "+reqs.size());
        }
    }
    
    public void processRequest(CriticalRequest toProcess) {
        //System.out.println("Thread "+id+" is processing req by "+toProcess.getRequester()+" request: His: "+latestRequest.getRequestTime()+" theirs: "+toProcess.getRequestTime());
        if(toProcess.getRequestTime() < latestRequest.getRequestTime()) {
            coworkers.get(toProcess.getRequester()).incrementAck();
            System.out.println("Thread "+id+" sent ack to "+toProcess.getRequester());
            reqs.remove(toProcess);
            System.out.println("Removed: Size of reqs for "+id+" is "+reqs.size());
        }
        else if (toProcess.getRequestTime() ==  latestRequest.getRequestTime() && this.id < toProcess.getRequester()) {
            coworkers.get(toProcess.getRequester()).incrementAck();
             System.out.println("Thread "+id+" sent ack to "+toProcess.getRequester());
             reqs.remove(toProcess);
             System.out.println("Removed: Size of reqs for "+id+" is "+reqs.size());
        }
        else if(toProcess.getRequestTime() > latestRequest.getRequestTime() && ( latestRequest.getDirection() != null  && latestRequest.getDirection().equals(toProcess.getDirection()))) {
             System.out.println("Thread "+id+" sent ack to "+toProcess.getRequester());
             coworkers.get(toProcess.getRequester()).incrementAck();
             reqs.remove(toProcess);
             System.out.println("Removed: Size of reqs for "+id+" is "+reqs.size());
        }
    }
   
    public void incrementAck() {
        
        synchronized(ackLock) {
            ackCount++;
            System.out.println("Ack count of "+id+" is "+ackCount);
        }
        
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
    
    public void setState(String state) {
        displayPanel.setState(state);
    }
    
    public String panelGetState() {
        return displayPanel.getState();
    }
    
    private void sendRequestOld() {
        latestRequest = new CriticalRequest(id, null);
        System.out.println("Thread "+id+" has sent request with timestamp "+latestRequest.getRequestTime());
        for(WorkingThread i : coworkers) {
            if( i.id != this.id) {
                i.addRequest(latestRequest);
            }
        }
    }
    
    private void sendRequestModified() {
        if(displayPanel.returnLoc().equals("right"))
            latestRequest = new CriticalRequest(id, "left");
        else
            latestRequest = new CriticalRequest(id, "right");
        for(WorkingThread i : coworkers) {
            if( i.id != this.id) {
                i.addRequest(latestRequest);
            }
        }
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
           if(latestRequest == null)
               sendRequestOld();
           if(!reqs.isEmpty()) {
               for(int i=0; i < reqs.size(); i++)
                   processRequest(reqs.get(i));
               
           }
           else {
               //System.out.println("Thread "+id+" has empty requests.");
           }
           if(ackCount == 3) { // Critical Section
               synchronized(critLock) {
                System.out.println("Thread "+id+" has entered CS.");
                if(displayPanel.returnLoc().equals("right"))
                    displayPanel.setState("left");
                else
                    displayPanel.setState("right");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkingThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(CriticalRequest i : reqs) {
                    coworkers.get(i.getRequester()).incrementAck();
                }
                reqs.clear();
                latestRequest = null;
                ackCount = 0;
                System.out.println("Thread "+id+" has exited CS.");
               }
           }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkingThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while(stillWorking);
        System.out.println("Thread "+id+" has stopped working.");
    }
    
    private void modRicartAgrawala() {
        do {
             setState("finished");
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            setState("right");
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while(stillWorking);
    }
}
