/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Kuba
 */
@Serializable
public class UpdateLobbyClientRequest extends AbstractMessage
{
    private boolean ready;
    private short team; //0 - no team
    
    public UpdateLobbyClientRequest(){}

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public short getTeam() {
        return team;
    }

    public void setTeam(short team) {
        this.team = team;
    }
    
}
