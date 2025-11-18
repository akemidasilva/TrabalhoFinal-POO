package Modelo;

import java.util.Random;

public class ZigueZague extends PersonagemAnimado {
    
    private int iContador;
    private int velocidade;
    private Random rand = new Random();
    private Estado ultimaDirecao = Estado.DIREITA;
    
    // inimigo3 move todas as direcoes
    public ZigueZague(String sNomeImagePNG, int linha, int coluna, int velocidade) {
        super(sNomeImagePNG, linha, coluna); 
        this.velocidade = velocidade;
        this.iContador = 0;
        this.bTransponivel = true;
        this.bMortal = true;
        
        String prefixo = sNomeImagePNG.replace(".png", "");
        animCima = carregarAnimacao(prefixo + "_cima_0.png", prefixo + "_cima_1.png");
        animBaixo = carregarAnimacao(prefixo + "_baixo_0.png", prefixo + "_baixo_1.png");
        animEsquerda = carregarAnimacao(prefixo + "_esq_0.png", prefixo + "_esq_1.png");
        animDireita = carregarAnimacao(prefixo + "_dir_0.png", prefixo + "_dir_1.png");
        
        this.iFrames = animDireita;
        this.animParado = animDireita;
    }

    // move aleatoriamente
    public void autoDesenho(){
        iContador++;
        if(iContador == velocidade) {
            iContador = 0;
            int iDirecao = rand.nextInt(4);
            
            if(iDirecao == 0){
                this.moveUp();
                ultimaDirecao = Estado.CIMA;
            } else if(iDirecao == 1){
                this.moveDown();
                ultimaDirecao = Estado.BAIXO;
            }else if(iDirecao == 2){
                this.moveLeft();
                ultimaDirecao = Estado.ESQUERDA;
            }else if(iDirecao == 3){
                this.moveRight();
                ultimaDirecao = Estado.DIREITA;
            }
        }
        
        this.estadoAtual = ultimaDirecao;
        
        switch(ultimaDirecao) {
            case CIMA: this.animParado = animCima; break;
            case BAIXO: this.animParado = animBaixo; break;
            case ESQUERDA: this.animParado = animEsquerda; break;
            case DIREITA: this.animParado = animDireita; break;
            default: break;
        }

        super.autoDesenho();
    }    
    
    @Override
    public void recarregarAnimacoes(String prefixo) {
        String nomeBase = prefixo.replace(".png", "");
        animCima = carregarAnimacao(nomeBase + "_cima_0.png", nomeBase + "_cima_1.png");
        animBaixo = carregarAnimacao(nomeBase + "_baixo_0.png", nomeBase + "_baixo_1.png");
        animEsquerda = carregarAnimacao(nomeBase + "_esq_0.png", nomeBase + "_esq_1.png");
        animDireita = carregarAnimacao(nomeBase + "_dir_0.png", nomeBase + "_dir_1.png");
        this.iFrames = animDireita;
        this.animParado = animDireita;
    }
}