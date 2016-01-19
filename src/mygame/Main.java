package mygame;

import Input.InputHandler;
import Network.GenerateMapOrder;
import Network.NetworkManager;
import Network.RejectClientOrder;
import Player.ServerPlayer;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.ArrayList;
import mygame.Server.ServerConnectionHandler;
import mygame.Server.ServerMain;
import mygame.Server.ServerMessageHandler;

/**
 * test
 * @author normenhansen
 */
import tonegod.gui.core.Screen;
public class Main extends SimpleApplication 
{
    public static Client client;
    public static ServerMain server = null;
    
    public static ArrayList<ServerPlayer> connectedPlayers;
    
    public static String clientName;
    
    public static ClientConnectionHandler clientConnectionHandler = null;
    public static ClientMessageHandler clientMessageHandler = null;
    
    Screen screen;

    public static GameAppState gameState;
    public static MenuAppState menuState;
    public static LobbyAppState lobbyState;
    public static InputHandler inputHandler;
    
    public static void main(String[] args) 
    {
        NetworkManager.initSerializables();
        
        Main app = new Main();
        app.start();
                
    }

    @Override
    public void simpleInitApp() 
    {    
        screen = new Screen(this);
        
        connectedPlayers = new ArrayList<ServerPlayer>();
        connectedPlayers.clear();
        
        gameState = new GameAppState(this, screen);
        menuState = new MenuAppState(this, screen);
        lobbyState = new LobbyAppState(this, screen);
        stateManager.attach(menuState);
        
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(50.0f);
        
        inputHandler = new InputHandler(this.inputManager);
        
        this.setDisplayStatView(true);
        
        
        clientConnectionHandler = new ClientConnectionHandler(this);
        clientMessageHandler = new ClientMessageHandler(this);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) 
    {
        
    }

    @Override
    public void simpleRender(RenderManager rm) 
    {
       
    }
    
    @Override
    public void destroy()
    {
        if(server != null)
        {
            RejectClientOrder msg = new RejectClientOrder();
            msg.setReason("Host disconnected");
            server.server.broadcast(msg);
            server.stop();
        }
        
        if(client != null)
        {
            if(client.isConnected())
            {
                client.close();
            }
        }
        
        super.destroy();
    }
}
