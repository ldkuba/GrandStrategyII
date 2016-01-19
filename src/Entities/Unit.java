/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Kuba
 */
public class Unit extends Entity
{
    Spatial model;
    
    int health;
    protected float speed;
    
    public Unit()
    {
        super();
        
        this.entityObject = new Node("LocalRootNode");
    }
    
    public void addHealth(int delta)
    {
        this.health += delta;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }
    
    public float getSpeed()
    {
        return this.speed;
    }
}
