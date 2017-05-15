package network.rpcprotocol;

import domain.User;
import network.dto.DTOUtils;
import network.dto.UserDTO;
import network.services.ConfException;
import network.services.IConfClient;
import network.services.IConfServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class ConfClientRpcWorker implements Runnable, IConfClient {

    //referinta la server + conexiunea
    private IConfServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ConfClientRpcWorker(IConfServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

//    aici inca nu am nici-o functie pentru client deoarece interfata IConfClient este goala

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request) {
        Response response = null;

        //daca request-ul e de login
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.type());
            UserDTO userDTO = (UserDTO) request.data();
            User user = DTOUtils.getFromDTO(userDTO);
            try {
                server.login(user, this);
                return okResponse;
            } catch (ConfException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        //daca request-ul e de logout
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            UserDTO userDTO = (UserDTO) request.data();
            User user = DTOUtils.getFromDTO(userDTO);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;

            } catch (ConfException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

}
