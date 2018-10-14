package web;

import java.util.Map;

public class IdentityCaptureRequest {
    private String image;
    private String to;
    private Map<String, Object> events;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, Object> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Object> events) {
        this.events = events;
    }
}
