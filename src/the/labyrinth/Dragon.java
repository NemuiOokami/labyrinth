package the.labyrinth;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author szabi
 */
public class Dragon extends Character{
    
    public Dragon(int r, int c)
    {
        super(r,c);
    }
    
    @Override
    public char characterType(){
    return 'd';
}
    
}
