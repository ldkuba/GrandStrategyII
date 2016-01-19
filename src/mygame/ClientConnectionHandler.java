/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Network.RegisterClientRequest;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;

/**
 *
 * @author Kuba
 */
public class ClientConnectionHandler implements ClientStateListener
{
    private SimpleApplication app;
    private boolean game;
    
    public ClientConnectionHandler(Application app)
    {
        this.app = (SimpleApplication) app;
        game = false;
    }
    
    public void switchToGame()
    {
        game = true;
    }
    
    public void clientConnected(Client c) 
    {
       if(game)
       {
           
       }else
       {
           RegisterClientRequest msg = new RegisterClientRequest(Main.clientName);
           c.send(msg);
       }
    }

    public void clientDisconnected(Client c, DisconnectInfo info) 
    {
        if(game)
        {
            this.app.getStateManager().attach(Main.menuState);
            this.app.getStateManager().detach(Main.gameState);
        }else
        {
            this.app.getStateManager().attach(Main.menuState);
            this.app.getStateManager().detach(Main.lobbyState);
        }
    }
    
}
