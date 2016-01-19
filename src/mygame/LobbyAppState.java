/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Network.GenerateMapOrder;
import Network.StartGameRequest;
import Network.UpdateGameParamsRequest;
import Network.UpdateLobbyClientRequest;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.network.HostedConnection;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.lists.ComboBox;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author Kuba
 */
public class LobbyAppState extends AbstractAppState
{
    private Screen screen;
    
    private SimpleApplication app;
    
    public final int MAX_PLAYERS = 8;//for now
    
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    Node localRootNode = new Node("LobbyRootNode");
    Node localGuiNode = new Node("LobbyGuiNode");
    
    private Element rootGuiElement;
    private Element backgroundElement;
    
    private Element[] playerLabels;
    private CheckBox[] readyButton;
    
    //private SelectBox[] teamLabels;
    
    private SelectBox teamSelection;
    private Element[] teamLabels;
    
    private ButtonAdapter startGameButton;
    
    private SelectBox sizeSelection;
    private SelectBox maxPlayersSelection;
    
    //settings params
    public static int mapSize;
    public int maxPlayerCount;
    
    
    private int tempMapSizeSelection = -1;
    private int tempMaxPlayersSelection = -1;
    
    
    public LobbyAppState(SimpleApplication app, Screen screen)
    {
        this.app = (SimpleApplication)app;
        
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.viewPort = app.getViewPort();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.screen = screen;
        
        rootGuiElement = new Element(this.screen, "Panel0Lobby", new Vector2f(0, 0),
                new Vector2f(app.getContext().getSettings().getWidth(), app.getContext().getSettings().getHeight()), 
                new Vector4f(0, 0, 0, 0), "Textures/transparent_default.png");
        
        
        //will be modified in update; default = 4
        //maxPlayerCount = 4;
        //mapSize = 512;
        
        backgroundElement = new Element(this.screen, "BackgroundLobby", new Vector2f(0, 0),
                new Vector2f(this.app.getContext().getSettings().getWidth(), this.app.getContext().getSettings().getHeight()),
                new Vector4f(0, 0, 0, 0), "Textures/menu_background.png");
        
         rootGuiElement.attachChild(backgroundElement);
        
        playerLabels = new Element[MAX_PLAYERS];
        readyButton = new CheckBox[MAX_PLAYERS];
        //teamLabels = new SelectBox[MAX_PLAYERS];
        teamLabels = new Element[MAX_PLAYERS];
        
        for(int i = 0; i < MAX_PLAYERS; i++)
        {
            playerLabels[i] = new Element(this.screen, "Player"+i+"Label", new Vector2f(150, 600 - i*50), new Vector2f(200, 50), new Vector4f(0, 0, 0, 0), null);
            playerLabels[i].setText("EMPTY");
            playerLabels[i].setFontColor(ColorRGBA.White);
            
            rootGuiElement.attachChild(playerLabels[i]);
            
            teamLabels[i] = new Element(this.screen, "Team"+i+"Label", new Vector2f(250, (this.app.getContext().getSettings().getHeight() - 650) + i*50), new Vector2f(200, 50), new Vector4f(0, 0, 0, 0), null);
            teamLabels[i].setText("");
            teamLabels[i].setFontColor(ColorRGBA.White);
            
            rootGuiElement.addChild(teamLabels[i]);
        }
        
        for(int i = 0; i < MAX_PLAYERS; i++)
        {
            readyButton[i] = new CheckBox(this.screen, ""+i, new Vector2f(400, (this.app.getContext().getSettings().getHeight() - 650) + i*50), new Vector2f(32, 32), new Vector4f(0, 0, 0, 0), "Textures/readyButtonOff.png")
            {
                @Override
                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled)
                {
                   
                    
                    if(Main.connectedPlayers.get(Integer.parseInt(this.getUID())).getId() == Main.client.getId())
                    {
                       
                        
                        if(toggled)
                        {
                           
                            Main.connectedPlayers.get(Integer.parseInt(this.getUID())).setReady(true);
                        }else
                        {
                            
                            Main.connectedPlayers.get(Integer.parseInt(this.getUID())).setReady(false);
                        }
                        
                        UpdateLobbyClientRequest msg = new UpdateLobbyClientRequest();
                        msg.setReady(Main.connectedPlayers.get(Integer.parseInt(this.getUID())).isReady());
                        msg.setTeam((short)Main.connectedPlayers.get(Integer.parseInt(this.getUID())).getTeam());
                        
                        Main.client.send(msg);
                    }else
                    {
                        System.out.println("No Match");
                    }
                }
            };
            
            System.out.println("CHECK BOXES FIREDD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            
            readyButton[i].setButtonHoverInfo("Textures/readyButtonOff.png", ColorRGBA.Yellow);
            
            readyButton[i].setButtonPressedInfo("Textures/readyButtonOn.png", ColorRGBA.Green);
            
            
            readyButton[i].hide();
            readyButton[i].move(0, 0, 1.0f);
            
            rootGuiElement.addChild(readyButton[i]);
            
            ///     
            
            
        }
        
        
        /*
        for(int i = 0; i < MAX_PLAYERS; i++)
        {
            
            teamLabels[i] = new SelectBox(this.screen, "TEAM" + i, new Vector2f(200, (this.app.getContext().getSettings().getHeight() - 650) + i*50), new Vector2f(100, 35))
            {
                @Override
                public void onChange(int selectedIndex, Object value) 
                {
                    if(Main.client == null)
                    {
                        System.out.println("UGABUGA\nUGABUGA\nUGABUGA\nUGABUGA\n");
                    }else
                    {
                        if(Integer.parseInt(this.getUID().replace("TEAM", "")) == Main.client.getId())
                        {
                            Main.connectedPlayers.get(Integer.parseInt(this.getUID().replace("TEAM", ""))).setTeam(selectedIndex);
                        }
                    }
                }
            };
            
            System.out.println("TEAM LABELS FIREDD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            
            teamLabels[i].addListItem("No Team", "0");
            teamLabels[i].addListItem("Team 1", "1");
            teamLabels[i].addListItem("Team 2", "2");
            teamLabels[i].addListItem("Team 3", "3");
            teamLabels[i].addListItem("Team 4", "4");
            teamLabels[i].addListItem("Team 5", "5");
            teamLabels[i].addListItem("Team 6", "6");
            teamLabels[i].addListItem("Team 7", "7");
            teamLabels[i].addListItem("Team 8", "8");
            
            teamLabels[i].pack();
            
            
           // rootGuiElement.addChild(teamLabels[i]);
        }
        * 
        */
        
        teamSelection = new SelectBox(this.screen, "team_selection", new Vector2f(500, 400), new Vector2f(100, 25))
        {
            @Override
            public void onChange(int selectedIndex, Object value)
            {
                if(Main.client != null)
                {
                    for(int i = 0; i < MAX_PLAYERS; i++)
                    {
                        if(i < Main.connectedPlayers.size())
                        {
                            if(Main.client.getId() == Main.connectedPlayers.get(i).getId())
                            {
                                Main.connectedPlayers.get(i).setTeam(selectedIndex);
                                
                                UpdateLobbyClientRequest msg = new UpdateLobbyClientRequest();
                                msg.setReady(Main.connectedPlayers.get(i).isReady());
                                msg.setTeam((short)Main.connectedPlayers.get(i).getTeam());
                        
                                Main.client.send(msg);
                            }
                        }
                    }
                }
            }
        };
        
        teamSelection.addListItem("No Team", "0");
        teamSelection.addListItem("Team 1", "1");
        teamSelection.addListItem("Team 2", "2");
        teamSelection.addListItem("Team 3", "3");
        teamSelection.addListItem("Team 4", "4");
        teamSelection.addListItem("Team 5", "5");
        teamSelection.addListItem("Team 6", "6");
        teamSelection.addListItem("Team 7", "7");
        teamSelection.addListItem("Team 8", "8");
        
        teamSelection.pack();
        
        rootGuiElement.addChild(teamSelection);
        
        backgroundElement.setIgnoreMouseButtons(true);
        
        this.localGuiNode.addControl(screen);
        
        
  
        
        
///////////////////////////////
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        
        startGameButton = new ButtonAdapter(this.screen, "StartGameButton", new Vector2f(500, 550))
        {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled)
            {
                StartGameRequest msg = new StartGameRequest();
                Main.client.send(msg);
            }
        };
        
        startGameButton.setText("Start");
        
        if(Main.server != null)
            rootGuiElement.addChild(startGameButton);
        
        sizeSelection = new SelectBox(this.screen, "size_Selection", new Vector2f(500, 450), new Vector2f(100, 25))
        {
            @Override
            public void onChange(int selectedIndex, Object value)
            {
                if(Main.client.isConnected())
                {
                    UpdateGameParamsRequest msg = new UpdateGameParamsRequest();
                    int size = (int)(Math.pow(2.0, (double)(selectedIndex)));
                    msg.setMapSize(size*256);
                    msg.setMaxPlayers(maxPlayerCount);
                    LobbyAppState.mapSize = size*256;
                    Main.client.send(msg);
                }
            }
        };
        
        sizeSelection.addListItem("256", "256");
        sizeSelection.addListItem("512", "512");
        sizeSelection.addListItem("1024", "1024");
        sizeSelection.addListItem("2048", "2048");
        
        if(Main.server == null)
        {
            sizeSelection.setIsEnabled(false);
            
        }
 
        rootGuiElement.addChild(sizeSelection);
        
        maxPlayersSelection = new SelectBox(this.screen, "maxPlayerSelect", new Vector2f(400, 600), new Vector2f(100, 25))
        {
            @Override
            public void onChange(int selectedIndex, Object value)
            {
                if(Main.client.isConnected())
                {
                    UpdateGameParamsRequest msg = new UpdateGameParamsRequest();
                    msg.setMapSize(LobbyAppState.mapSize);
                    msg.setMaxPlayers(selectedIndex+1);
                    maxPlayerCount = selectedIndex+1;
                    Main.client.send(msg);
                }
            }
        };
        
        maxPlayersSelection.addListItem("1", "1");
        maxPlayersSelection.addListItem("2", "2");
        maxPlayersSelection.addListItem("3", "3");
        maxPlayersSelection.addListItem("4", "4");
        maxPlayersSelection.addListItem("5", "5");
        maxPlayersSelection.addListItem("6", "6");
        maxPlayersSelection.addListItem("7", "7");
        maxPlayersSelection.addListItem("8", "8");
        
        if(Main.server == null)
        {
            maxPlayersSelection.setIsEnabled(false);
        }
        
        
        rootGuiElement.addChild(maxPlayersSelection);
        
        if(this.tempMapSizeSelection != -1)
        {
            sizeSelection.setSelectedIndex(this.tempMapSizeSelection);
            maxPlayersSelection.setSelectedIndex(this.tempMaxPlayersSelection);
            this.maxPlayerCount = this.tempMaxPlayersSelection+1;
        }else
        {
            sizeSelection.setSelectedIndex(1);
            maxPlayersSelection.setSelectedIndex(3);
        }
        
        screen.addElement(this.rootGuiElement);
        
        this.localGuiNode.addControl(screen);
        
        rootNode.attachChild(localRootNode);  
        guiNode.attachChild(localGuiNode);   
        
        if(Main.client.isConnected() && Main.server != null)
        {
            LobbyAppState.mapSize = 512;
            maxPlayerCount = 4;  
            
            UpdateGameParamsRequest msg = new UpdateGameParamsRequest();
            msg.setMapSize(LobbyAppState.mapSize);
            msg.setMaxPlayers(maxPlayerCount);
            Main.client.send(msg);
        }
               
    }
    
    public void updateGameParams(int size, int maxPlayers)
    {
        LobbyAppState.mapSize = size;
        maxPlayerCount = maxPlayers;
        System.out.println("The recieved max players is actually: " + maxPlayers + "  and the static number hacbeen se to: " + maxPlayerCount);
        
        int a = size/256;
        
        double b = Math.log10(a)/Math.log10(2);
        Math.round(b);
        int c = (int)(b);
        
        if(this.maxPlayersSelection != null)
        {
            this.sizeSelection.setSelectedIndex(c);
            this.maxPlayersSelection.setSelectedIndex(maxPlayers-1);
        }else
        {
            this.tempMapSizeSelection = c;
            this.tempMaxPlayersSelection = maxPlayers-1;
        }
    }
    
    @Override
    public void update(float tpf)
    {        
        System.out.println(this.maxPlayerCount);
        
        System.out.println("MAx Players:" + maxPlayerCount);
        
        for(int i = 0; i < MAX_PLAYERS; i++)
        {
            
            if(i < maxPlayerCount)
            {
                if(Main.connectedPlayers.size() > i)
                {
                    //System.out.println( Main.connectedPlayers.get(i).getId() + "  ?=  " + Main.client.getId());
                    
                    System.out.println(Main.connectedPlayers.get(i).isReady());
                    
                    playerLabels[i].setText(Main.connectedPlayers.get(i).getName());
                    
                    readyButton[i].show();
          
                    
                    if(Main.connectedPlayers.get(i).getTeam() == 0)
                    {
                        teamLabels[i].setText("No Team");
                    }else
                    {
                        teamLabels[i].setText("Team " + Main.connectedPlayers.get(i).getTeam());
                    }
              
                    System.out.println(Main.connectedPlayers.get(i).isReady());
                    
                    if(Main.connectedPlayers.get(i).getId() != Main.client.getId())
                    {
                        if(Main.connectedPlayers.get(i).isReady())
                        {
                            System.out.println("Yes");
                            readyButton[i].setIsToggled(true);
                        }else
                        {
                            System.out.println("No");
                            readyButton[i].setIsToggled(false);
                        }
                    }
                    
                    if((int)Main.connectedPlayers.get(i).getId() == Main.client.getId())
                    {
                        readyButton[i].setIgnoreMouseLeftButton(false);
                        //teamLabels[i].controlIsEnabledHook(false);
                      
          
                    }else
                    {
                        readyButton[i].setIgnoreMouseLeftButton(true);
                        //teamLabels[i].controlIsEnabledHook(true);
                        
                        
                    }
                    
                }else
                {
                    playerLabels[i].setText("EMPTY");
                    readyButton[i].hide();
                    //rootGuiElement.detachChild(teamLabels[i]);
                    teamLabels[i].setText("");
                }
            }else
            {
                playerLabels[i].setText("CLOSED");
                readyButton[i].hide();
                //rootGuiElement.detachChild(teamLabels[i]);
                teamLabels[i].setText("");
            }
        }
        
        //System.out.println("Connections no.: " + Main.connectedPlayers.size());
    }
    
    @Override
    public void cleanup()
    {
        super.cleanup();
        
        this.localGuiNode.removeControl(screen);
        screen.removeElement(this.rootGuiElement);
        
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        if(enabled)
        {
            
        }else
        {
            
        }
    }
}
