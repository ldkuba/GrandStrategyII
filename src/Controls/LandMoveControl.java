/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controls;

import Entities.TestUnit;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Kuba
 */
public class LandMoveControl extends AbstractControl
{
    
    private TestUnit testUnit;
    private float speed;
    
    public LandMoveControl(TestUnit testUnit)
    {
        this.testUnit = testUnit;
        speed = this.testUnit.getSpeed();
    }

    @Override
    protected void controlUpdate(float tpf) 
    {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) 
    {
        
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        
    }

    public void move(Vector3f destination) 
    {
        
    }
    
}
 