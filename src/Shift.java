import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES; //Used to convert the lunch and shift duration into minutes later

public class Shift {
    
    private int id;
    private String description;
    private LocalTime shiftstart;
    private LocalTime shiftstop;
    private int interval;
    private int gracePeriod;
    private int dock;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private int lunchDeduct;
    private long lunchDuration;  
    private long shiftDuration; 

    public Shift(int id, String description, LocalTime shiftstart, LocalTime shiftstop, int interval, int gracePeriod, int dock,
            LocalTime lunchStart, LocalTime lunchStop, int lunchDeduct, long lunchDuration) 
    {
        this.id = id;
        this.description = description;
        this.shiftstart = shiftstart;
        this.shiftstop = shiftstop;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.lunchDeduct = lunchDeduct;
        this.lunchDuration = MINUTES.between(lunchStart, lunchStop) ;
        this.shiftDuration = MINUTES.between(shiftstart, shiftstop);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getShiftstart() {
        return shiftstart;
    }

    public void setShiftstart(LocalTime shiftstart) {
        this.shiftstart = shiftstart;
    }

    public LocalTime getShiftstop() {
        return shiftstop;
    }

    public void setShiftstop(LocalTime shiftstop) {
        this.shiftstop = shiftstop;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getDock() {
        return dock;
    }

    public void setDock(int dock) {
        this.dock = dock;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(LocalTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }

    public void setLunchStop(LocalTime lunchStop) {
        this.lunchStop = lunchStop;
    }

    public int getLunchDeduct() {
        return lunchDeduct;
    }

    public void setLunchDeduct(int lunchDeduct) {
        this.lunchDeduct = lunchDeduct;
    }

    public long getLunchDuration() {
        return lunchDuration;
    }

    public void setLunchDuration(long lunchDuration) {
        this.lunchDuration = lunchDuration;
    }

    public long getShiftDuration() {
        return shiftDuration;
    }

    public void setShiftDuration(long shiftDuration) {
        this.shiftDuration = shiftDuration;
    }
    
    
    @Override
    public String toString() {
        
        //Print output as in feature 1 test
        StringBuilder s = new StringBuilder();
        
        s.append("Shift ").append(id).append(": ").append(description).append(": ").append(shiftstart).append(" - ");
        s.append(shiftstop).append(" (").append(shiftDuration).append(" minutes); Lunch: ").append(lunchStart).append(" - ");
        s.append(lunchStop).append(" (").append(lunchDuration).append(" minutes)");
        
        return s.toString();
    }
    
    
    
    
}


 

