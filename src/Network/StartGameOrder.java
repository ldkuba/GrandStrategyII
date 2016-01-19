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
public class StartGameOrder extends AbstractMessage
{
    private int mapSize;
    private String lvlName;
    
    //params of game - future
    public StartGameOrder(int mapSize, String lvlName)
    {
        this.mapSize = mapSize;
        this.lvlName = lvlName;
    }
    
    public StartGameOrder(){}//Serialization
   

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }
    
    
}
