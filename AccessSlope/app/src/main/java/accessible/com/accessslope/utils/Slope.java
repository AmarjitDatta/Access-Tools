package accessible.com.accessslope.utils;

import java.util.Date;

public class Slope {
    private double azimuth;
    private double pitch;
    private double roll;
    private String timeStamp;

    public Slope(double azimuth, double pitch, double roll) {
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
        this.timeStamp = new Date().toString();
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
