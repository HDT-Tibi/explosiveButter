package network.utils;

import network.rpcprotocol.ConfClientRpcWorker;
import network.services.IConfServer;

import java.net.Socket;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public class ConfRpcConcurrentServer extends AbsConcurrentServer {

    private IConfServer appServer;

    public ConfRpcConcurrentServer(int port, IConfServer appServer) {
        super(port);
        this.appServer = appServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ConfClientRpcWorker worker=new ConfClientRpcWorker(appServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

}
