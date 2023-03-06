/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package the.labyrinth;
import java.awt.Image;
/**
 *
 * @author szabi
 */
public class Player extends Character{
   
        public Player(int r, int c)
    {
        super(r,c);
    }
        
        @Override
    public char characterType()
    {
        return 'p';
    }
    
}
