package Modelo;

//import java.io.Serializable;

public class Fruta extends Personagem /*implements Serializable*/ {
    
    // fruta n tem colisao e nao mata
    public Fruta(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna); 
        this.bTransponivel = true; 
        this.bMortal = false;
    }
}