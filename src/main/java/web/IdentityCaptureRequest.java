package web;

import lombok.Data;

import java.util.Map;

@Data
public class IdentityCaptureRequest {
    private String image;
    private String to;
    private Map<String, Object> events;
}
