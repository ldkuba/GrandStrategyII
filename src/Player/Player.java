/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import Entities.Unit;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Kuba
 */
public class Player 
{   
    protected short id;
    
    public boolean local;
    
    public ArrayList<Unit> units;
    
    public String name = "";
    
    public Node localRootNode;
    
    public Player()
    {
        
    }
    
}
