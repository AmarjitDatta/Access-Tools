package accessible.com.accesssound.utils;

import java.util.Date;

public class Noise {
    private double originalNoise;
    private double roundedNoise;
    private String timeStamp;

    public Noise(double pOriginalNoise) {
        this.originalNoise = pOriginalNoise;
        this.timeStamp = new Date().toString();
        this.roundedNoise = UtilityFunctions.round(pOriginalNoise, 2);
    }

    public double getOriginalNoise() {
        return originalNoise;
    }

    public void setOriginalNoise(double originalNoise) {
        this.originalNoise = originalNoise;
    }

    public double getRoundedNoise() {
        return roundedNoise;
    }

    public void setRoundedNoise(double roundedNoise) {
        this.roundedNoise = roundedNoise;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
