/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Input.InputHandler.Mappings;
import Network.NetworkManager;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.network.Network;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.io.IOException;
import mygame.Server.ServerMain;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author Kuba
 */
public class MenuAppState extends AbstractAppState
{
    private Screen screen;
    
    private SimpleApplication app;
    
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    Node localRootNode = new Node("GameRootNode");
    Node localGuiNode = new Node("GameGuiNode");
    
    private Element rootGuiElement;
    private Element backgroundElement;
    
    private ButtonAdapter host_button;
    private ButtonAdapter join_button;
    private ButtonAdapter setNicknameButton;
    
    private TextField nicknameField;
    
    private Element nicknameLabel;
    
    public MenuAppState(SimpleApplication app, Screen screen)
    {
        this.app = (SimpleApplication)app;
        
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.viewPort = app.getViewPort();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.screen = screen;
        
        
        
        rootGuiElement = new Element(this.screen, "Panel0Menu", new Vector2f(0, 0),
                new Vector2f(app.getContext().getSettings().getWidth(), app.getContext().getSettings().getHeight()), 
                new Vector4f(0, 0, 0, 0), "Textures/transparent_default.png");
        
        backgroundElement = new Element(this.screen, "BackgroundMenu", new Vector2f(0, 0),
                new Vector2f(this.app.getContext().getSettings().getWidth(), this.app.getContext().getSettings().getHeight()),
                new Vector4f(0, 0, 0, 0), "Textures/menu_background.png");
        
        nicknameLabel = new Element(screen, "nicknameLabel", new Vector2f(400, app.getContext().getSettings().getHeight() - 200), new Vector2f(500, 32), new Vector4f(0, 0, 0, 0), null);
        nicknameLabel.setText("Logged in as: ");
        nicknameLabel.setFontColor(ColorRGBA.White);
        
        nicknameField = new TextField(screen, "textfield", new Vector2f(400, 100), new Vector2f(150, 32), new Vector4f(0, 0, 0, 0), "Textures/textField.png");
        nicknameField.setMaxLength(12);
        nicknameField.setFontColor(ColorRGBA.White);
        
        host_button = new ButtonAdapter(screen, "buttonhost", new Vector2f(400, 200)) {

            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent arg0, boolean arg1) 
            {
                if(Main.server == null)
                {
                    Main.server = new ServerMain();
                
                    try{
                        Main.client = Network.connectToServer("127.0.0.1", NetworkManager.PORT);
                        Main.client.start();
                    }catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }
                
                Main.client.addMessageListener(new ClientMessageHandler(this.app));
                Main.client.addClientStateListener(new ClientConnectionHandler(this.app));
                }
                
                this.app.getStateManager().detach(Main.menuState);
                this.app.getStateManager().attach(Main.lobbyState);
            }
        };
        
        host_button.setText("Host Game");
        
        join_button = new ButtonAdapter(screen, "buttonjoin", new Vector2f(400, 300)) {

            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent arg0, boolean arg1) 
            {
                if(Main.server == null)
                {
                    try{
                        Main.client = Network.connectToServer("127.0.0.1", NetworkManager.PORT);
                        Main.client.start();
                    }catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }
                
                Main.client.addMessageListener(new ClientMessageHandler(this.app));
                Main.client.addClientStateListener(new ClientConnectionHandler(this.app));
   
                }
                
                this.app.getStateManager().detach(Main.menuState);
                this.app.getStateManager().attach(Main.lobbyState);
                
            }
        };   
        
        setNicknameButton = new ButtonAdapter(screen, "buttonsetnickname", new Vector2f(270, app.getContext().getSettings().getHeight() - 130), new Vector2f(100, 32))
        {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent arg0, boolean arg1)
            {
                if(!nicknameField.getText().isEmpty())
                    nicknameLabel.setText("Logged in as: " + nicknameField.getText());
                    Main.clientName = nicknameField.getText();
            }
        };
        
        setNicknameButton.setText("set nick");
        
        
        join_button.setText("Join Game");
        
        
        
        rootGuiElement.addChild(backgroundElement);
        
        rootGuiElement.attachChild(nicknameField);
        rootGuiElement.addChild(host_button);
        rootGuiElement.addChild(join_button);
        rootGuiElement.addChild(nicknameLabel);
        rootGuiElement.addChild(setNicknameButton);
        
        nicknameField.move(0, 0, 1.0f);
        nicknameLabel.move(0, 0, 1.0f);
        setNicknameButton.move(0, 0, 1.0f);
        
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);

        screen.addElement(this.rootGuiElement);
        
        this.localGuiNode.addControl(screen);
        
        rootNode.attachChild(localRootNode);  
        guiNode.attachChild(localGuiNode);
        
        System.out.println("Elements: " + rootGuiElement.getChildren().size());
            
    }
    
    @Override
    public void update(float tpf)
    {
        System.out.println("X: " + inputManager.getCursorPosition().x + " Y: " + inputManager.getCursorPosition().y);
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
