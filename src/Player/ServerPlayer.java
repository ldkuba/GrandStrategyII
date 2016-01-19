/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import Player.Player;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Kuba
 */

@Serializable
public class ServerPlayer extends Player
{
    
    private boolean ready;
    private int team; //0 - no team, 1-8 teams
    
    public ServerPlayer(){}
    
    public ServerPlayer(short id, String name)
    {
        this.id = id;
        this.name = name;
        this.ready = false;
        this.team = 0;
        local = false;
    }
    
    public short getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setReady(boolean ready)
    {
        this.ready = ready;
    }
    
    public boolean isReady()
    {
        return this.ready;
    }
    
    public void setId(short id)
    {
        this.id = id;
    }
    
    public void setTeam(int team)
    {
        this.team = team;
    }
    
    public int getTeam()
    {
        return this.team;
    }
}
