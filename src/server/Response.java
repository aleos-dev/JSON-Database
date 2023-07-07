package server;

public class Response {
    private String response;
    private String value;
    private String reason;

    public static Builder builder() {
        return new Builder();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public static class Builder {
        private final Response response;

        private Builder() {
            this.response = new Response();
        }

        public Builder response(String response) {
            this.response.setResponse(response);
            return this;
        }

        public Builder value(String value) {
            response.setValue(value);
            return this;
        }

        public Builder reason(String reason) {
            response.setReason(reason);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}
