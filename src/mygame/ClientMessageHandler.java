/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Network.GenerateMapOrder;
import Network.NetworkManager;
import Network.RejectClientOrder;
import Network.StartGameOrder;
import Network.UpdateConnectedClientsListOrder;
import Network.UpdateGameParamsRequest;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mygame.GameAppState.terrainManager;

/**
 *
 * @author Kuba
 */
public class ClientMessageHandler implements MessageListener<Client>
{
    private boolean game;
    private SimpleApplication app;
    
    public ClientMessageHandler(Application app)
    {
        game = false;
        this.app = (SimpleApplication) app;
    }
    
    public void switchToGame()
    {
        game = true;
    }

    public void messageReceived(Client source, Message m) 
    {
        if(game)
        {
            if(m instanceof GenerateMapOrder)
            {
                //GenerateMapOrder msg = (GenerateMapOrder) m;
        
                //GameAppState.terrainManager.generateNoiseMap(msg.getLvlName(), msg.getSize());
                
            }else if(m instanceof RejectClientOrder)
            {
                RejectClientOrder msg = (RejectClientOrder) m;
                System.out.println(msg.getReason());
                source.close();

                app.getStateManager().detach(Main.gameState);
                app.getStateManager().attach(Main.menuState);
            
            }
        
           
        }else
        {
           /* if(m instanceof UpdateConnectedClientsListOrder)
            {
                UpdateConnectedClientsListOrder msg = (UpdateConnectedClientsListOrder) m;
                
                Main.connectedPlayers = msg.getConnections();
            }*/
            
            if(m instanceof RejectClientOrder)
            {
                RejectClientOrder msg = (RejectClientOrder) m;
                System.out.println(msg.getReason());
                source.close();

                app.getStateManager().detach(Main.lobbyState);
                app.getStateManager().attach(Main.menuState);
            
                    
            }else if(m instanceof UpdateConnectedClientsListOrder)
            {
                UpdateConnectedClientsListOrder msg = (UpdateConnectedClientsListOrder) m;
                
                System.out.println("Players: " + msg.getConnections());
                
                Main.connectedPlayers = msg.getConnections();
                
            }else if(m instanceof UpdateGameParamsRequest)
            {
                UpdateGameParamsRequest msg = (UpdateGameParamsRequest) m;
                
                if(Main.server == null)
                {    
                    System.out.println("The recieved max players is: " + msg.getMaxPlayers());
                    Main.lobbyState.updateGameParams(msg.getMapSize(), msg.getMaxPlayers());
                }  
                
            }else if(m instanceof StartGameOrder)
            {
                this.switchToGame();
                //load game
                StartGameOrder msg = (StartGameOrder)m;
                
                app.getStateManager().detach(Main.lobbyState);
                app.getStateManager().attach(Main.gameState);
                
                System.out.println("TERRAIN IS GENERATED");
                System.out.println("SIZE OF TERRAIN IS: "+ msg.getMapSize());
                GameAppState.terrainManager.generateNoiseMap(msg.getLvlName(), msg.getMapSize());
                
          
            }
           
        }
    }
    
}
