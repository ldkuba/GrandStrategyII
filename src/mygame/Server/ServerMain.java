/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Server;

import Network.NetworkManager;
import Network.RejectClientOrder;
import Network.StartGameOrder;
import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.Collection;
import Player.*;
import java.util.ArrayList;

/**
 *
 * @author Kuba
 */
public class ServerMain extends SimpleApplication
{
    public Server server;
    
    public ServerConnectionHandler serverConnectionHandler = null;
    public ServerMessageHandler serverMessageHandler = null;
    public ArrayList<ServerPlayer> players;
    
    public int max_players;
    public int mapSize;
    
    boolean game = false;
    
    public ServerMain()
    {
        NetworkManager.initSerializables();
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(30);
        
        mapSize = 512;
        
        max_players = 4;
        
        this.setSettings(newSetting);
        this.start(JmeContext.Type.Headless);
        
        serverConnectionHandler = new ServerConnectionHandler(this);
        serverMessageHandler = new ServerMessageHandler(this);
        
        players = new ArrayList<ServerPlayer>();
        players.clear();
    }

    @Override
    public void simpleInitApp()
    {
        try{
            server = Network.createServer(NetworkManager.PORT);
            server.start();
            max_players = 4;
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        server.addMessageListener(new ServerMessageHandler(this)); 
        server.addConnectionListener(new ServerConnectionHandler(this));
        
        max_players = 4;//default
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        
    }
    
    public Collection<HostedConnection> getConnections()
    {
        return server.getConnections();
    }
    
    public void switchToGame()
    {
        
    }
    
    @Override
    public void destroy()
    {
 
        if(server.isRunning())
        {
            RejectClientOrder msg = new RejectClientOrder();
            msg.setReason("Host left the game!");
            server.broadcast(msg);
            
            while(server.getConnections().size() != 0)
            {
                System.out.print("Waiting for clients to disconnect");
            }
            
            server.close();
        }
        
        super.destroy();
    }
}