package accessible.com.utils;

import java.util.Date;

public class Light {
    private String timeStamp;
    private String lightIntensity;

    public Light(String lightIntensity) {
        this.lightIntensity = lightIntensity;
        this.timeStamp = new Date().toString();
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(String lightIntensity) {
        this.lightIntensity = lightIntensity;
    }
}
