package Modelo;

//import java.io.Serializable;

public class Gelo extends Personagem /*implements Serializable*/ {
    
    private boolean indestrutivel = false; 

    // gelo tem colisao e nao mata
    public Gelo(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna); 
        this.bTransponivel = false;
        this.bMortal = false;
    }
    
    // bordas inquebraveis
    public void setIndestrutivel(boolean indestrutivel) {
        this.indestrutivel = indestrutivel;
    }
    
    public boolean isIndestrutivel() {
        return this.indestrutivel;
    }
}