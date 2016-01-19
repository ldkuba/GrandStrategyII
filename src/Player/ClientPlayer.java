/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import Entities.Unit;
import Player.Player;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Kuba
 */
public class ClientPlayer extends Player
{
    
    private InputManager inputManager;
    private AssetManager assetManager;
    
    private SimpleApplication app;
    private Node rootNode;
    private Node guiNode;
    
    private Node localGuiNode;
    
    private PlayerGui gui;
    
    private ArrayList<Unit> selectedUnits;
    
    public ClientPlayer(short id, Application app, Node rootNode, Node guiNode)
    {
        this.id = id;
        this.app = (SimpleApplication) app;
        this.rootNode = rootNode;
        this.guiNode = guiNode;
        this.inputManager = app.getInputManager();
        this.assetManager = app.getAssetManager();
        
        this.localRootNode = new Node("localRootNode");
        this.localGuiNode = new Node("localGuiNode");
        
        gui = new PlayerGui(localGuiNode);
        
        selectedUnits = new ArrayList<Unit>();
        selectedUnits.clear();
        
        units = new ArrayList<Unit>();
        units.clear();
        
        local = true;
        
        init();
        
        
    }
    
    public void init()
    {
        //load resources(models, images)
        gui.init();
                //attatch
        guiNode.attachChild(localGuiNode);
        rootNode.attachChild(localRootNode);
        
    }
    
    public void update()
    {
        
    }
}
