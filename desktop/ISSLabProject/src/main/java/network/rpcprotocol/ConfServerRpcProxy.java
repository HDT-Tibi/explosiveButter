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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class ConfServerRpcProxy implements IConfServer {

    //connection details
    private String host;
    private int port;

    //clientul (pt fiecare client se face cate un proxy)
    private IConfClient client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ConfServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(User user, IConfClient client) throws ConfException {
        initializeConnection();

        UserDTO userDTO = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();

        sendRequest(req);

        Response response = readResponse();

        if (response.type() == ResponseType.OK)
            System.out.println("user logat cu succes"); //s-a logat cu succes
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ConfException(err);
        }
    }

    @Override
    public void logout(User user, IConfClient client) throws ConfException {
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(userDTO).build();

        sendRequest(req);

        Response response = readResponse();

//        closeConnection();
        if (response.type() == ResponseType.OK) {
            System.out.println("user delogat"); //s-a delogat cu succes
            closeConnection();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConfException(err);
        }
    }

    @Override
    public void register(User user) throws ConfException {
        initializeConnection();

        UserDTO userDTO = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.REGISTER).data(userDTO).build();

        sendRequest(req);

        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            System.out.println("user inregistrat cu succes"); //s-a inregistrat cu succes
            closeConnection();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ConfException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ConfException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ConfException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ConfException {
        Response response = null;
        try {
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ConfException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
//        aici prinde response-urile de la worker, inca nu am nimic deoarece interfata IConfClient e goala
    }

    private boolean isUpdate(Response response) {
//        aici verifica daca response-ul citit mai jos este de tip update
        return false; //placeholder
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

}
