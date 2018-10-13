package sai1.model;

public class IdentityValidationResponse {
    private final boolean success;
    private final String identityData;

    public IdentityValidationResponse(boolean success, String identityData) {
        this.success = success;
        this.identityData = identityData;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getIdentityData() {
        return identityData;
    }
}
