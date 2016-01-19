/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Kuba
 */

@Serializable
public class UnitMoveRequest extends AbstractMessage
{
    public short playerId, unitId;
    public Vector3f position;
    
    public UnitMoveRequest(short playerId, short unitId, Vector3f position)
    {
        this.playerId = playerId;
        this.unitId = unitId;
        this.position = position;
    }
    
    public UnitMoveRequest(){};
}
