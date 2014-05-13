/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewells.distosproj;

/**
 *
 * @author Ethan
 */
public class CriticalRequest {
    
    private final long reqTime;
    private final int requester;
    private String direction = null;
    
    public CriticalRequest(int requester, String direction) {
        reqTime = System.nanoTime();
        this.requester = requester;
        this.direction = direction;
    }
    
    public long getRequestTime() {
        return reqTime;
    }
    
    public int getRequester() {
        return requester;
    }
    
    public String getDirection() {
        return direction;
    }
    
}
