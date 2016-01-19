/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import com.jme3.scene.Node;

/**
 *
 * @author Kuba
 */
public class PlayerGui 
{
    private Node guiNode;
    private Node localGuiNode;
    
    public PlayerGui(Node guiNode)
    {
        this.guiNode = guiNode;
        
        localGuiNode = new Node("localGuiNode");
    }
    
    public void init()
    {
        //load images
        
        
        
        //attatch to parent
        guiNode.attachChild(localGuiNode);
    }
    
}
