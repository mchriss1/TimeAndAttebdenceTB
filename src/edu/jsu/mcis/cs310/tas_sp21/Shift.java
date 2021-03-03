package edu.jsu.mcis.cs310.tas_sp21;

import java.time.LocalTime;

public class Shift {
    
    private String shiftDescription;
    private LocalTime shiftStart;
    private LocalTime shiftStop;
    private int shiftGrace;
    private int shiftInterval;
    private int shiftDock;
    private LocalTime shiftLunchStart;
    private LocalTime shiftLunchStop;
    private int shiftLunchDeduct;
    private LocalTime shiftLunchDuration;
    
    public Shift(String ShiftDescription, int shiftStartHour, int shiftStartMin, 
            int shiftStopHour, int shiftStopMin, int shiftGrace, int shiftInterval, int shiftDock, 
            int LunchStartHour,int LunchStartMin, int LunchStopHour, 
            int LunchStopMin, int LunchDeduct){
        
        this.shiftDescription = ShiftDescription;
        this.shiftStart = LocalTime.of(shiftStartHour, shiftStartMin);
        this.shiftStop = LocalTime.of(shiftStopHour, shiftStopMin);
        this.shiftGrace = shiftGrace;
        this.shiftInterval = shiftInterval;
        this.shiftDock = shiftDock;
        this.shiftLunchStart = LocalTime.of(LunchStartHour, LunchStartMin);
        this.shiftLunchStop = LocalTime.of(LunchStopHour, LunchStopMin);
        this.shiftLunchDeduct = LunchDeduct;
    }
    
    // getter methods

    public LocalTime getShiftStart() {
        return shiftStart;
    }
    
    public int getShiftStartHour(){
        return getShiftStart().getHour();
    }
    
    public int getShiftStartMin(){
        return getShiftStart().getMinute();
    }
        
    public LocalTime getShiftStop() {
        return shiftStop;
    }
    public int getShiftStopHour(){
        return getShiftStop().getHour();
    }
    
    public int getShiftStopMin(){
        return getShiftStop().getMinute();
    }
    
    public int getShiftGrace() {
        return shiftGrace;
    }

    public int getShiftInterval() {
        return shiftInterval;
    }
    
    public int getShiftDock() {
        return shiftDock;
    }
    
    public LocalTime getShiftLunchStart() {
        return shiftLunchStart;
    }
    
    public LocalTime getShiftLunchStop() {
        return shiftLunchStop;
    }
    
    public int getShiftLunchDeduct() {
        return shiftLunchDeduct;
    }

    //setter methods;
    
    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public void setShiftStop(LocalTime shiftStop) {
        this.shiftStop = shiftStop;
    }


    public void setShiftGrace(int shiftGrace) {
        this.shiftGrace = shiftGrace;
    }

    public void setShiftInterval(int shiftInterval) {
        this.shiftInterval = shiftInterval;
    }

    public void setShiftDock(int shiftDock) {
        this.shiftDock = shiftDock;
    }

    public void setShiftLunchStart(LocalTime shiftLunchStart) {
        this.shiftLunchStart = shiftLunchStart;
    }

    public void setShiftLunchStop(LocalTime shiftLunchStop) {
        this.shiftLunchStop = shiftLunchStop;
    }

    public void setShiftLunchDeduct(int shiftLunchDeduct) {
        this.shiftLunchDeduct = shiftLunchDeduct;
    }

    public LocalTime getShiftLunchDuration() {
        return shiftLunchDuration;
    }

    public void setShiftLunchDuration(LocalTime shiftLunchDuration) {
        this.shiftLunchDuration = shiftLunchDuration;
    }
    
    // for total shift hours 
    public int totalShiftDuration(){
        int totalShiftStartTime = (getShiftStart().getHour()*60) + 
                getShiftStart().getMinute();
        
        int totalShiftStopTime = (getShiftStop().getHour()*60) + 
                getShiftStop().getMinute();
        
        return (totalShiftStopTime - totalShiftStartTime);
    }
    
    // for Lunch duration (LunchStart - LunchStop)
    
    public int totalLunchDuration(){
        int totalLunchStartTime = (getShiftLunchStart().getHour()*60) +
                getShiftLunchStart().getMinute();
        
        int totalLunchStopTime = (getShiftLunchStop().getHour()*60) +
                getShiftLunchStop().getMinute();
        
        return (totalLunchStopTime - totalLunchStartTime);
    }
    
    @Override
    public String toString(){
        
        StringBuilder sb = new StringBuilder();
        sb.append(shiftDescription).append(": ").append(shiftStart.toString())
                .append(" - ");
        sb.append(shiftStop.toString()).append(" (")
                .append(totalShiftDuration());
        sb.append(" minutes); Lunch: ").append(shiftLunchStart.toString())
                .append(" - ");
        sb.append(shiftLunchStop.toString()).append(" (")
                .append(totalLunchDuration()).append(" minutes)");
        
        return sb.toString();
    }
    
}
