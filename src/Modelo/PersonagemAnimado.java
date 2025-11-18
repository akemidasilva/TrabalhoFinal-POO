package Modelo;

import Auxiliar.Desenho;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class PersonagemAnimado extends Personagem {

    // animacoes
    public ArrayList<ImageIcon> animParado;
    public ArrayList<ImageIcon> animCima;
    public ArrayList<ImageIcon> animBaixo;
    public ArrayList<ImageIcon> animEsquerda;
    public ArrayList<ImageIcon> animDireita;
    public ArrayList<ImageIcon> animAtaque;
    protected ArrayList<ImageIcon> animMorrendo;
    
    protected enum Estado { PARADO, CIMA, BAIXO, ESQUERDA, DIREITA, ATACANDO, MORRENDO }
    protected Estado estadoAtual = Estado.PARADO;
    protected boolean isMorto = false; 

    public PersonagemAnimado(int linha, int coluna) {
        super(linha, coluna); 
    }
    
    public PersonagemAnimado(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna); 
    }

    // mov invalido
    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    // verifica posicao
    protected boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        } 
        return true;       
    }

    // seleciona animacao para cada estado
    @Override
    public void autoDesenho() {
        if (isMorto && animMorrendo != null && iFrameAtual == animMorrendo.size() - 1) {
            super.autoDesenho(); 
            return;
        } else {
            switch (estadoAtual) {
                case PARADO: iFrames = animParado; break;
                case CIMA: iFrames = animCima; break;
                case BAIXO: iFrames = animBaixo; break;
                case ESQUERDA: iFrames = animEsquerda; break;
                case DIREITA: iFrames = animDireita; break;
                case ATACANDO: iFrames = animAtaque; break;
                case MORRENDO: iFrames = animMorrendo; break;
                default: iFrames = animParado;
            }

            if(iFrames == null) {
                iFrames = animParado;
            }
            
            if(iFrames == null || iFrames.isEmpty()) {
                super.autoDesenho();
                return;
            }
            
            if (iFrameAtual >= iFrames.size()) iFrameAtual = 0;
            
            super.autoDesenho();
            
            if (estadoAtual == Estado.ATACANDO && iFrameAtual == iFrames.size() - 1) {
                estadoAtual = Estado.PARADO; 
            } else if (estadoAtual != Estado.ATACANDO && estadoAtual != Estado.MORRENDO) {
                estadoAtual = Estado.PARADO; 
            }

            } 
        }
    
    @Override
    public boolean moveUp() {
        if (isMorto) return false;
        this.estadoAtual = Estado.CIMA;
        if(super.moveUp()) 
            return validaPosicao(); 
        return false;
    }

    @Override
    public boolean moveDown() {
        if (isMorto) return false;
        this.estadoAtual = Estado.BAIXO;
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    @Override
    public boolean moveRight() {
        if (isMorto) return false;
        this.estadoAtual = Estado.DIREITA;
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    @Override
    public boolean moveLeft() {
        if (isMorto) return false;
        this.estadoAtual = Estado.ESQUERDA;
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }
    
    public boolean isMorto() {
        return isMorto;
    }
    
    public boolean isAnimacaoMorteCompleta() {
        if (!isMorto || animMorrendo == null || animMorrendo.isEmpty()) {
            return false; 
        }
        return iFrameAtual == animMorrendo.size() - 1;
    }
    
    public abstract void recarregarAnimacoes(String prefixo);
}