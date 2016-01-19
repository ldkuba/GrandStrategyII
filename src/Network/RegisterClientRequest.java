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
public class RegisterClientRequest extends AbstractMessage
{
    private String name;
    
    public RegisterClientRequest(){}
    
    public RegisterClientRequest(String name)
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
}
