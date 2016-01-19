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
public class UpdateGameParamsRequest extends AbstractMessage
{
    //game params
    private int mapSize;
    private int maxPlayers;
    
    public UpdateGameParamsRequest(){}

    public int getMapSize() 
    {
        return mapSize;
    }

    public void setMapSize(int mapSize) 
    {
        this.mapSize = mapSize;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
