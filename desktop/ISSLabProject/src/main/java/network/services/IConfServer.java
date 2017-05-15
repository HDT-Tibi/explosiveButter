package network.services;

import domain.User;

/**
 * Created by Bitten Apple on 15-May-17.
 */
public interface IConfServer {

    public void login(User user, IConfClient client) throws ConfException;
    public void logout(User user, IConfClient client) throws ConfException;
    public void register(User user) throws ConfException;

}
