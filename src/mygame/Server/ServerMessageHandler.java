/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Server;

import Network.GenerateMapOrder;
import Network.RegisterClientRequest;
import Network.RejectClientOrder;
import Network.StartGameOrder;
import Network.StartGameRequest;
import Network.UpdateConnectedClientsListOrder;
import Network.UpdateGameParamsRequest;
import Network.UpdateLobbyClientRequest;
import Player.ServerPlayer;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author Kuba
 */
public class ServerMessageHandler implements MessageListener<HostedConnection>
{
    private boolean game;
    private ServerMain serverApp;
    
    public ServerMessageHandler(){}
    
    public ServerMessageHandler(ServerMain serverApp)
    {
        game = false;
        this.serverApp = serverApp;
        
    }
    
    public void switchToGame()
    {
        game = true;
    }
    
    public void messageReceived(HostedConnection source, Message m)
    {
        if(game)
        {
            
        }else
        {
            if(m instanceof RegisterClientRequest)
            {
                RegisterClientRequest msg = (RegisterClientRequest) m;
                
                System.out.println("Recieved Request from " + source.getId());
                
                if(this.serverApp.max_players < this.serverApp.server.getConnections().size())
                {
                    System.out.println("Request rejected from " + source.getId() + "  Max players is " + this.serverApp.max_players + "   Currently connected: " + this.serverApp.players.size());
                    
                    RejectClientOrder msg2 = new RejectClientOrder("Server is full");
                    this.serverApp.server.broadcast(Filters.equalTo(source), msg2);
                    
                    
                }else
                {
                    ServerPlayer player = new ServerPlayer((short) source.getId(), msg.getName());
                    this.serverApp.players.add(player);
                    
                    UpdateConnectedClientsListOrder updateMsg = new UpdateConnectedClientsListOrder(this.serverApp.players);
                    
                    this.serverApp.server.broadcast(updateMsg);
                    
                    UpdateGameParamsRequest updateMsg2 = new UpdateGameParamsRequest();
                    updateMsg2.setMapSize(this.serverApp.mapSize);
                    updateMsg2.setMaxPlayers(this.serverApp.max_players);
                    
                    this.serverApp.server.broadcast(Filters.equalTo(source), updateMsg2);
                  
                    System.out.println("Request accepted from " + source.getId() + "  Max players is " + this.serverApp.max_players + "   Currently connected: " + this.serverApp.players.size());
                    
                    
                }
                
            }else if(m instanceof UpdateLobbyClientRequest)
            {
                UpdateLobbyClientRequest msg = (UpdateLobbyClientRequest) m;
                
                for(int i = 0; i < this.serverApp.players.size(); i++)
                {
                    if(this.serverApp.players.get(i).getId() == source.getId())
                    {
                        this.serverApp.players.get(i).setReady(msg.isReady());
                        this.serverApp.players.get(i).setTeam(msg.getTeam());
                    }
                }
                
                UpdateConnectedClientsListOrder updMsg = new UpdateConnectedClientsListOrder();
                updMsg.setConnections(this.serverApp.players);
                
                this.serverApp.server.broadcast(updMsg);
                
            }else if(m instanceof UpdateGameParamsRequest)
            {
                UpdateGameParamsRequest msg = (UpdateGameParamsRequest) m;
                
                this.serverApp.mapSize = msg.getMapSize();
                this.serverApp.max_players = msg.getMaxPlayers();
                
                UpdateGameParamsRequest updMsg = new UpdateGameParamsRequest();
                updMsg.setMapSize(msg.getMapSize());
                updMsg.setMaxPlayers(msg.getMaxPlayers());
                
                this.serverApp.server.broadcast(Filters.notEqualTo(source), updMsg);
                
            }else if(m instanceof StartGameRequest)
            {
                int checkSum = 0;
                
                for(int i = 0; i < this.serverApp.players.size(); i++)
                {
                    if(this.serverApp.players.get(i).isReady())
                    {
                        System.out.println("Player "+i+" ready!");
                        checkSum++;
                    }
                }
                
                if(checkSum == this.serverApp.players.size())
                {
                    System.out.println("IT IS DONE");
                    this.switchToGame();
                    this.serverApp.switchToGame();
                    //broadcast start game message
                    
                    StartGameOrder msg = new StartGameOrder();
            
                    msg.setMapSize(this.serverApp.mapSize);
                    msg.setLvlName("TestName");
                    
                    this.serverApp.server.broadcast(msg);
                    
                }
                
            }
        }
    }
    
}
