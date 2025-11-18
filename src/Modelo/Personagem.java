package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Personagem implements Serializable {

    protected ArrayList<ImageIcon> iFrames; // frames animacao
    protected int iFrameAtual = 0;
    protected int iContadorFrames = 0;
    protected int FRAME_DELAY = 2; // velocidade da animacao (1 = mais rapido)

    protected Posicao pPosicao;
    protected boolean bTransponivel; 
    protected boolean bMortal;   
    protected String sNomeImagePNG;    

    public boolean isbMortal() {
        return bMortal;
    }
    
    protected Personagem(String sNomeImagePNG, int linha, int coluna) {
        this.sNomeImagePNG = sNomeImagePNG;
        this.pPosicao = new Posicao(linha, coluna); 
        this.bTransponivel = true;
        this.bMortal = false;
        
        this.iFrames = new ArrayList<>(); 
        try {
            ImageIcon iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
            Image img = iImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            
            this.iFrames.add(new ImageIcon(bi)); 
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String getNomeImagem() {
    return this.sNomeImagePNG;
}
    
    protected Personagem(int linha, int coluna) {
        this.pPosicao = new Posicao(linha, coluna);
        this.bTransponivel = true;
        this.bMortal = false;
        this.iFrames = new ArrayList<>(); // Apenas inicializa
    }

    // cria animacao
    public ArrayList<ImageIcon> carregarAnimacao(String... nomesArquivos) {
        ArrayList<ImageIcon> listaFrames = new ArrayList<>();
        try {
            String path = new java.io.File(".").getCanonicalPath() + Consts.PATH;
            
            for (String nome : nomesArquivos) {
                ImageIcon iImage = new ImageIcon(path + nome);
                Image img = iImage.getImage();
                BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                listaFrames.add(new ImageIcon(bi));
            }
        } catch (IOException ex) {
            System.out.println("Erro carregando animação: " + ex.getMessage());
        }
        return listaFrames;
    }

    // avanca frame
    protected void proximoFrame() {
        if (iFrames == null || iFrames.isEmpty()) return;
        
        iContadorFrames++;
        if (iContadorFrames % FRAME_DELAY == 0) {
            iContadorFrames = 0;
            iFrameAtual = (iFrameAtual + 1) % iFrames.size(); // Volta ao início
        }
    }

    // imprime frame na tela
    public void autoDesenho(){
        proximoFrame(); 
        
        if (iFrames != null && !iFrames.isEmpty()) {
            Desenho.desenhar(this.iFrames.get(iFrameAtual), this.pPosicao.getColuna(), this.pPosicao.getLinha());
        }
    }

    public Posicao getPosicao() { return pPosicao; }
    public boolean isbTransponivel() { return bTransponivel; }
    public void setbTransponivel(boolean bTransponivel) { this.bTransponivel = bTransponivel; }
    public boolean setPosicao(int linha, int coluna) { return pPosicao.setPosicao(linha, coluna); }
    public boolean moveUp() { return this.pPosicao.moveUp(); }
    public boolean moveDown() { return this.pPosicao.moveDown(); }
    public boolean moveRight() { return this.pPosicao.moveRight(); }
    public boolean moveLeft() { return this.pPosicao.moveLeft(); }
}