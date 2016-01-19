/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Kuba
 */

@Serializable
public class UnitMoveOrder
{
    public short playerId, unitId;
    //public Path path;
    
    public UnitMoveOrder(short playerId, short unitId/*, Path path*/)
    {
        this.playerId = playerId;
        this.unitId = unitId;
        //this.path = path;
        
    
    }
    
    public UnitMoveOrder(){}
}
