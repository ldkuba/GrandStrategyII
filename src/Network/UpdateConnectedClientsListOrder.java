/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Player.ServerPlayer;
import com.jme3.network.AbstractMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Kuba
 */

@Serializable
public class UpdateConnectedClientsListOrder extends AbstractMessage
{
    private ArrayList<ServerPlayer> connections;
    
    
    public UpdateConnectedClientsListOrder(){}
    
    
    public UpdateConnectedClientsListOrder(ArrayList<ServerPlayer> connections)
    {
        this.connections = connections;
    }
    
    public ArrayList<ServerPlayer> getConnections()
    {
        return this.connections;
    }
    
    public void setConnections(ArrayList<ServerPlayer> connections)
    {
        this.connections = connections;
    } 
}
