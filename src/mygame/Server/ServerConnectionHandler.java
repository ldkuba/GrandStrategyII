/*
 * To change this template, choose Tools | Templatess
 * and open the template in the editor.
 */
package mygame.Server;

import Network.GenerateMapOrder;
import Network.UpdateConnectedClientsListOrder;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

/**
 *
 * @author Kuba
 */
public class ServerConnectionHandler implements ConnectionListener
{
    private boolean game;
    
    ServerMain serverApp;
    
    public ServerConnectionHandler(SimpleApplication serverApp)
    {
        this.serverApp = (ServerMain) serverApp;
        
        game = false;
    }
    
    public void switchToGame()
    {
        game = true;
    }
    
    public void connectionAdded(Server server, HostedConnection conn)
    {
        if(game)
        {
            //conn.send(new GenerateMapOrder("Sample Level", 512));
        }else
        {
            
        }
    }

    public void connectionRemoved(Server server, HostedConnection conn)
    {
        if(game)
        {
            for(int i = 0; i < this.serverApp.players.size(); i++)
            {
                if(this.serverApp.players.get(i).getId() == conn.getId())
                {
                    this.serverApp.players.remove(i);
                }
            }
            
            UpdateConnectedClientsListOrder msg = new UpdateConnectedClientsListOrder();
            msg.setConnections(this.serverApp.players);
            
            server.broadcast(msg);
        }else
        {
            for(int i = 0; i < this.serverApp.players.size(); i++)
            {
                if(this.serverApp.players.get(i).getId() == conn.getId())
                {
                    this.serverApp.players.remove(i);
                }
            }
            
            UpdateConnectedClientsListOrder msg = new UpdateConnectedClientsListOrder();
            msg.setConnections(this.serverApp.players);
            
            server.broadcast(msg);
        
        }
    }
    
}
