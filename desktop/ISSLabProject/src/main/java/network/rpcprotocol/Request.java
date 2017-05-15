package network.rpcprotocol;

import java.io.Serializable;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class Request implements Serializable {

    //field-uri
    private RequestType type;
    private Object data;

    //constructor pt builderul de mai jos
    private Request() {
    }

    //getters
    public RequestType type() {
        return type;
    }

    public Object data() {
        return data;
    }

    //setters
    private void data(Object data) {
        this.data = data;
    }

    private void type(RequestType type) {
        this.type = type;
    }

    //builder
    public static class Builder {
        private Request request = new Request();

        public Builder type(RequestType type) {
            request.type(type);
            return this;
        }

        public Builder data(Object data) {
            request.data(data);
            return this;
        }

        public Request build() {
            return request;
        }
    }

}
