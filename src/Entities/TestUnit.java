/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Controls.LandMoveControl;
import com.jme3.asset.AssetManager;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.File;
import java.io.IOException;
import Player.*;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;

public class TestUnit extends Unit
{    
    private LandMoveControl moveControl;
    
    public TestUnit()
    {
        super();
        this.speed = 2.0f;
        this.entityObject.addControl(new LandMoveControl(this));
        this.position = new Vector3f(0, 0, 0);
    }
    
    public TestUnit(Vector3f pos, AssetManager assetManager, Player player)
    {
        super();
        this.speed = 2.0f;
        this.entityObject.addControl(new LandMoveControl(this));
        this.position = pos;
        
        model = assetManager.loadModel("Models/person.j3o");
        Material defaultMat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        model.setMaterial(defaultMat);
        model.setLocalTranslation(this.position);
        this.entityObject.attachChild(model);
        
        player.units.add(this);
        
        player.localRootNode.attachChild(this.entityObject);
    }
    
    public void move(Vector3f destination)
    {
        moveControl.move(destination);
    }
    
}