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
public class GenerateMapOrder extends AbstractMessage
{ 
    String lvlName;
    int size;

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
    public GenerateMapOrder(String lvlName, int size)
    {
        this.lvlName = lvlName;
        this.size = size;
    }
    
    public GenerateMapOrder(){}
    
    
}
