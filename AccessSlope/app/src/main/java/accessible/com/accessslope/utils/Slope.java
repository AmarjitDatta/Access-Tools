package accessible.com.accessslope.utils;

import java.util.Date;

public class Slope {
    private double originalSlope;
    private double roundedSlope;
    private String timeStamp;

    public Slope(double pOriginalSlope) {
        this.originalSlope = pOriginalSlope;
        this.timeStamp = new Date().toString();
        this.roundedSlope = UtilityFunctions.round(pOriginalSlope, 2);
    }

    public double getOriginalSlope() {
        return originalSlope;
    }

    public void setOriginalSlope(double originalSlope) {
        this.originalSlope = originalSlope;
    }

    public double getRoundedSlope() {
        return roundedSlope;
    }

    public void setRoundedSlope(double roundedSlope) {
        this.roundedSlope = roundedSlope;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
