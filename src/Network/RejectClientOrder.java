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
public class RejectClientOrder extends AbstractMessage
{
    private String reason;
    
    public RejectClientOrder(){}
    
    public RejectClientOrder(String reason)
    {
        this.reason = reason;
    }
    
    public String getReason()
    {
        return this.reason;
    }
    
    public void setReason(String reason)
    {
        this.reason = reason;
    }
    
}
