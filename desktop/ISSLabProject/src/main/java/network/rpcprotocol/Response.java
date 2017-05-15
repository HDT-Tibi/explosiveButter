package network.rpcprotocol;

import java.io.Serializable;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class Response implements Serializable {

    //field-uri
    private ResponseType type;
    private Object data;

    //constructor pt builderul de mai jos
    private Response() {

    }

    //getters
    public ResponseType type() {
        return type;
    }

    public Object data() {
        return data;
    }

    //setters
    private void type(ResponseType type) {
        this.type = type;
    }

    private void data(Object data) {
        this.data = data;
    }

    //builder
    public static class Builder {
        private Response response = new Response();

        public Builder type(ResponseType type) {
            response.type(type);
            return this;
        }

        public Builder data(Object data) {
            response.data(data);
            return this;
        }

        public Response build() {
            return response;
        }
    }

}
