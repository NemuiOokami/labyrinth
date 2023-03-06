
package the.labyrinth;

public abstract class Character {
    
    private int posR;
    private int posC;
    
    public Character(int r,int c)
    {
        posR = r;
        posC = c;
    }

    public int getPosR() {
        return posR;
    }

    public int getPosC() {
        return posC;
    }

    protected void setPosR(int posR) {
        this.posR = posR;
    }

    protected void setPosC(int posC) {
        this.posC = posC;
    }
    
    abstract protected char characterType();
}
