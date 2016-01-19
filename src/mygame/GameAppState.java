/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Input.InputHandler;
import Input.InputHandler.Mappings;
import Network.NetworkManager;
import Util.NavMeshUtils;
import com.jme3.ai.navmesh.NavMesh;
import org.critterai.nmgen.NavmeshGenerator;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.Network;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainPatch;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import jme3tools.optimize.GeometryBatchFactory;
import static mygame.Main.client;
import mygame.Server.ServerMain;
import org.critterai.nmgen.TriangleMesh;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import Entities.*;
import Player.ClientPlayer;
import Player.Player;

/**
 *
 * @author Kuba
 */
class GameAppState extends AbstractAppState
{
    SimpleApplication app;
    
    Player localPlayer;
    
    Element rootGuiElement;
    
    private NavMesh navMesh;
    private NavmeshGenerator generator;
    private Mesh navmeshMesh;
    
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(2);
    
    private Screen screen;
    
    private float MoveSpeed = 20.0f;
    private float ZoomSpeed = 200.0f;
    
    private Vector3f camLocation = new Vector3f(0, 20, 0);
    private Vector3f lookAtDirection = new Vector3f(0, -0.8f, -0.2f);
    private float camDistance = 7.0f;
    
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    Node localRootNode = new Node("GameRootNode");
    Node localGuiNode = new Node("GameGuiNode");
    
    private Camera cam;
    
    public static TerrainManager terrainManager;
    
    Unit testUnit;
    
    
    GameAppState(SimpleApplication app, Screen screen)
    {
   
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.viewPort = app.getViewPort();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.cam = app.getCamera();
        this.screen = screen;
        
        terrainManager = new TerrainManager(assetManager, localRootNode);
        terrainManager.init((short)1);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
        
        rootNode.attachChild(localRootNode);  
        guiNode.attachChild(localGuiNode);
        
        localPlayer = new ClientPlayer((short)0, this.app, this.localRootNode, this.localGuiNode);
        
        generator = new NavmeshGenerator(10.0f, 10.0f, 16.0f, 4.0f, 45.0f, true, 4.0f, 2, false, 3, 4, 1.0f, 6.0f, 1, 0.5f, 0.6f);
        
        this.cam = app.getCamera();
        cam.lookAtDirection(lookAtDirection, Vector3f.UNIT_Y);
        camLocation.set(cam.getDirection().mult(-camDistance));
        cam.setLocation(camLocation);
        
        ButtonAdapter button = new ButtonAdapter(screen, "button", new Vector2f(15f, 15f)) {

            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent arg0, boolean arg1) 
            {
                Main.inputHandler.setInputState(Mappings.KeyS, true);
            }
        };
        
        rootGuiElement = new Element(this.screen, "Panel0", new Vector2f(0, 0), new Vector2f(this.app.getContext().getSettings().getWidth(), this.app.getContext().getSettings().getHeight()), new Vector4f(0, 0, 0, 0), "Textures/transparent_default.png");
        rootGuiElement.addChild(button);
        this.screen.addElement(rootGuiElement);
        this.localGuiNode.addControl(screen);
        
       // terrainManager.flatten(-150, -150, 50, 50);
        
            /** A white, directional light source */ 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        localRootNode.addLight(sun); 
        
        while(terrainManager.terrain == null)
        {
            System.out.println("WAITING FOR TERRAIN");
        }
        
        if(navmeshMesh == null)
            initNavMesh();
        
        testUnit = new TestUnit(new Vector3f(30.0f, -200.0f, 30.0f), this.assetManager, localPlayer);
    }    
    
    public void initNavMesh()
    {
        NavMeshUtils nmUtils = new NavMeshUtils(assetManager);
        navmeshMesh = nmUtils.buildMeshForNavMesh(terrainManager.terrain);
        
        Geometry navGeo = new Geometry("navMesh", navmeshMesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Green);
        navGeo.setMaterial(mat);
        navGeo.move(0, 0.1f, 0);
        localRootNode.attachChild(navGeo);      
        
        navMesh = new NavMesh();
        navMesh.loadFromMesh(navmeshMesh);
                
    }
    
    @Override
    public void update(float tpf)
    {
        System.out.println("X: " + this.cam.getLocation().x + "Y: " + this.cam.getLocation().y + "Z: " + this.cam.getLocation().z);
        
        if(Main.inputHandler.getInputState(Mappings.KeyUp))
        {
            camLocation.addLocal(new Vector3f(0, 0, -1).multLocal(MoveSpeed).multLocal(tpf));
            cam.setLocation(camLocation);
        }
        
        if(Main.inputHandler.getInputState(Mappings.KeyDown))
        {
            camLocation.addLocal(new Vector3f(0, 0, 1).multLocal(MoveSpeed).multLocal(tpf));
            cam.setLocation(camLocation);
        }
        
        if(Main.inputHandler.getInputState(Mappings.KeyLeft))
        {
            camLocation.addLocal(new Vector3f(-1, 0, 0).multLocal(MoveSpeed).multLocal(tpf));
            cam.setLocation(camLocation);
        }
        
        if(Main.inputHandler.getInputState(Mappings.KeyRight))
        {
            camLocation.addLocal(new Vector3f(1, 0, 0).multLocal(MoveSpeed).multLocal(tpf));
            cam.setLocation(camLocation);
        }
        
        if(Main.inputHandler.getInputState(Mappings.MouseWheelUp))
        {
            camLocation.addLocal(cam.getDirection().mult(-ZoomSpeed*tpf));
            cam.setLocation(camLocation);
            Main.inputHandler.setInputState(Mappings.MouseWheelUp, false);
        }
        
        
        if(Main.inputHandler.getInputState(Mappings.MouseWheelDown))
        {
            camLocation.addLocal(cam.getDirection().mult(ZoomSpeed*tpf));
            cam.setLocation(camLocation);
            Main.inputHandler.setInputState(Mappings.MouseWheelDown, false);
        }
        
        if(Main.inputHandler.getInputState(Mappings.KeyS))
        {
            if(this.app.getFlyByCamera().isEnabled())
            {
                this.app.getFlyByCamera().setEnabled(false);
                inputManager.setCursorVisible(true);
            }else
            {
                this.app.getFlyByCamera().setEnabled(true);
                inputManager.setCursorVisible(false);
            }
            
            Main.inputHandler.setInputState(Mappings.KeyS, false);
        }
        
        
        
    }
    
    @Override
    public void cleanup()
    {
        super.cleanup();
        exec.shutdown();
        
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
        
        if(Main.server != null)
        {
            Main.server.stop();
        }
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
