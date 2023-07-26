package server;

public class Response {

    private String response;
    private Object value;
    private String reason;

    private Response() {
    }

    public static Builder builder() {
        return new Builder();
    }

    private void setResponse(String response) {
        this.response = response;
    }

    private void setValue(Object value) {
        this.value = value;
    }

    private void setReason(String reason) {
        this.reason = reason;
    }

    public static class Builder {
        private final Response response;

        private Builder() {
            this.response = new Response();
        }

        public Builder status(String status) {
            this.response.setResponse(status);
            return this;
        }

        public Builder value(Object value) {
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

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                ", value=" + value +
                ", reason='" + reason + '\'' +
                '}';
    }
}
