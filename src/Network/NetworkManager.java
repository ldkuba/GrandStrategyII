/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Player.ServerPlayer;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Kuba
 */
public class NetworkManager
{
    public static final int PORT = 45454;
    
    @Serializable
    public static void initSerializables()
    {
        Serializer.registerClass(UnitMoveRequest.class);
        Serializer.registerClass(UnitMoveOrder.class);
        Serializer.registerClass(GenerateMapOrder.class);
        Serializer.registerClass(StartGameOrder.class);
        Serializer.registerClass(UpdateConnectedClientsListOrder.class);
        Serializer.registerClass(RegisterClientRequest.class);
        Serializer.registerClass(RejectClientOrder.class);
        Serializer.registerClass(ServerPlayer.class);
        Serializer.registerClass(UpdateLobbyClientRequest.class);
        Serializer.registerClass(StartGameRequest.class);
        Serializer.registerClass(UpdateGameParamsRequest.class);
    }
}
