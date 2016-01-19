/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;

/**
 *
 * @author Kuba
 */
public class InputHandler implements ActionListener, AnalogListener
{
    private InputManager inputManager;
    
    public static String[] MappingsStr =
    {
       "KeyUp", "KeyDown", "KeyLeft", "KeyRight", "MouseWheelUp", "MouseWheelDown", "KeyS", "KeyC"
    };
    
    public static enum Mappings
    {
        KeyUp, KeyDown, KeyLeft, KeyRight, MouseWheelUp, MouseWheelDown, KeyS, KeyC
    }
    
    public static boolean[] inputStates = new boolean[MappingsStr.length];
    
    public InputHandler(InputManager inputManager)
    {
        this.inputManager = inputManager;
        
        inputManager.addMapping(MappingsStr[0], new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(MappingsStr[1], new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(MappingsStr[2], new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(MappingsStr[3], new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(MappingsStr[4], new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(MappingsStr[5], new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(MappingsStr[6], new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(MappingsStr[7], new KeyTrigger(KeyInput.KEY_C));
        
        inputManager.addListener(this, MappingsStr);
    }
    
    public boolean getInputState(Mappings id)
    {
        return inputStates[id.ordinal()];
    }
    
    public void setInputState(Mappings id, boolean value)
    {
        inputStates[id.ordinal()] = value;
    }
    
    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if(name.equals(MappingsStr[Mappings.KeyUp.ordinal()]))
        {
            inputStates[Mappings.KeyUp.ordinal()] = isPressed;
        }
        
        if(name.equals(MappingsStr[Mappings.KeyDown.ordinal()]))
        {
            inputStates[Mappings.KeyDown.ordinal()] = isPressed;
        }
        
        if(name.equals(MappingsStr[Mappings.KeyLeft.ordinal()]))
        {
            inputStates[Mappings.KeyLeft.ordinal()] = isPressed;
        }
        
        if(name.equals(MappingsStr[Mappings.KeyRight.ordinal()]))
        {
            inputStates[Mappings.KeyRight.ordinal()] = isPressed;
        }
        
        if(name.equals(MappingsStr[Mappings.KeyS.ordinal()]))
        {
            inputStates[Mappings.KeyS.ordinal()] = isPressed;
        }
        
        if(name.equals(MappingsStr[Mappings.KeyC.ordinal()]))
        {
            inputStates[Mappings.KeyC.ordinal()] = isPressed;
        }
    }

    public void onAnalog(String name, float value, float tpf) 
    {
        if(name.equals(MappingsStr[Mappings.MouseWheelUp.ordinal()]))
        {
            inputStates[Mappings.MouseWheelUp.ordinal()] = true;
        }
        
        if(name.equals(MappingsStr[Mappings.MouseWheelDown.ordinal()]))
        {
            inputStates[Mappings.MouseWheelDown.ordinal()] = true;
        }
    }
}
