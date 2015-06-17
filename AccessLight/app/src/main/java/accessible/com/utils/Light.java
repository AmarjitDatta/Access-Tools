package accessible.com.utils;

public class Light {
    private String timeStamp;
    private String lightIntensity;

    public Light(String lightIntensity) {
        this.lightIntensity = lightIntensity;
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
