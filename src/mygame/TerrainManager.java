/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.terrain.noise.fractal.FractalSum;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kuba
 */

public class TerrainManager
{
    public TerrainQuad terrain;
    //private AbstractHeightMap heightmap;
    private Material terrainMat;
    
    private String lvlName;
    
    private int size;
    
    private AssetManager assetManager;
    
    public Node terrainNode;
    private Node rootNode;
    
    private Geometry marker;
    
    public TerrainManager( AssetManager assetManager, Node rootNode)
    {
        this.rootNode = rootNode;
        
        this.assetManager = assetManager;
        
        terrainNode = new Node("TerrainNode");
        
       
    }
    
    //loat textures and materials
    public void init(short type)
    {
        
        terrainMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        terrainMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/tempMap.png"));
        //terrainMat.getAdditionalRenderState().setWireframe(true);

        
    }
    
    public void generateNoiseMap(String lvlName, int size)//inne parametry
    {
        System.out.println("Terrain is surely being generated now! With size: " + size);
        
        this.lvlName = lvlName;
        this.size = size;
        
        float[] heightmap = new float[size*size];
        
        for(int i = 0; i < size*size; i++)
        {
            int x = i%size;
            int y = i/size;
            
            if(x > size/2 - 50 && x < size/2 + 50 && y > size/2 - 20 && y < size/2 + 20)
            {
                heightmap[i] = 10.0f;
            }else
            {
                heightmap[i] = 0.0f;
            }
        }
        
        /*
        HillHeightMap.NORMALIZE_RANGE = 50;
        
        try{
            heightmap = new AbstractHeightMap(size+1, 40000, 30, 80, 9098363l);
            heightmap.flatten((byte)3);
            for(int i = 0; i < 64; i++)
                heightmap.smooth(0.4f);
                
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        */ 
        
        makeTerrain(heightmap);
    }
    
   /* private static void generateImage(float[][] terrain){
        int size = terrain.length;
        int grey;
        
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                double result = terrain[x][y];
                
                grey = (int) (result * 255);
                int color = (grey << 16) | (grey << 8) | grey;
                img.setRGB(x, y, color);
                   
            }
        }
        
        try {
            ImageIO.write(img, "png", new File("assets/" + PATH));
        } catch (IOException ex) {
            System.out.println("Cannot save heightmap");
        }
    }
    */
    public void makeTerrain(float[] map)
    {   
        System.out.println("Is this actually happening??");
        
        terrain = new TerrainQuad(lvlName, 17, size+1, map);
        
        System.out.println("terrain:"+ terrain);
        terrain.setLocalTranslation(0, -200.0f, 0);
        terrain.setMaterial(terrainMat);
        
        terrainNode.detachAllChildren();
        terrainNode.attachChild(terrain);
        attatch();
    }
    
    public void flatten(int x, int z, int w, int h)
    {
        terrain.setLocked(false);
        
        float sum = 0;
        float mean = 0;
        
        for(int j = z; j < z+h; j++)
        {
            for(int i = x; i < x+w; i++)
            {
                sum += terrain.getHeight(new Vector2f(i, j));
            }
        }
        
        mean = sum/(w*h);
        
        for(int j = z; j < z+h; j++)
        {
            for(int i = x; i < x+w; i++)
            {
                terrain.setHeight(new Vector2f(i, j), mean);
            }
        }
        
        //smooth sides
        
        int smoothRadius = 10;
        
        for(int j = z; j < z+h; j++)
        {
            for(int i = x-smoothRadius; i < x; i++)
            {
                
                float gradient = (mean-(terrain.getHeight(new Vector2f(x-smoothRadius, j))))/(smoothRadius);
                
                float newH = gradient*(i-x) + mean;
                
                terrain.setHeight(new Vector2f(i, j), newH);
            }
        }
        
        for(int j = z; j < z+h; j++)
        {
            for(int i = x+w; i < x+w+smoothRadius; i++)
            {
                
                float gradient = ((terrain.getHeight(new Vector2f(x+w+smoothRadius, j)))-mean)/(smoothRadius);
                
                float newH = gradient*(i-(x+w)) + mean;
                
                terrain.setHeight(new Vector2f(i, j), newH);
            }
        }
        
        for(int i = x; i < x+w; i++)
        {
            for(int j = z-smoothRadius; j < z; j++)
            {
                
                float gradient = (mean-(terrain.getHeight(new Vector2f(i, z-smoothRadius))))/(smoothRadius);
                
                float newH = gradient*(j-z) + mean;
                
                terrain.setHeight(new Vector2f(i, j), newH);
            }
        }
        
        
        
        for(int i = x; i < x+w; i++)
        {
            for(int j = z+h; j < z+h+smoothRadius; j++)
            {
                
                float gradient = ((terrain.getHeight(new Vector2f(i, z+h+smoothRadius)))-mean)/(smoothRadius);
                
                float newH = gradient*(j-(z+h)) + mean;
                
                terrain.setHeight(new Vector2f(i, j), newH);
            }
        }
        
        //smooth corners
       
        
        /*int xDiagonal = 0;
        int yDiagonal = 0;
        int substractControl = 0;
        
        for(int j = z-smoothRadius; j < z; j++)
        {
            for(int i = x-smoothRadius; i < x-smoothRadius+substractControl+1; i++)
            {
                
            }
            
            substractControl++;
        }*/
        
        
        
        terrain.setLocked(true);
        
        terrain.forceRefresh(false, false, true);
    }
    
    public void detatch()
    {
        rootNode.detachChild(terrainNode);
    }
    
    public void attatch()
    {
        rootNode.attachChild(terrainNode);
    }
    
}
