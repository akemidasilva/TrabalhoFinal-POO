package Modelo;

import Auxiliar.Desenho;

public class Hero extends PersonagemAnimado {
    
    private int orientacao; // 0=cima, 1=baixo, 2=esquerda, 3=direita

    // hero move todas as direcoes
    public Hero(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);         
        this.orientacao = 1; 
        animParado = carregarAnimacao("hero_parado_0.png", "hero_parado_1.png"); 
        animBaixo = carregarAnimacao("hero_baixo_0.png", "hero_baixo_1.png", "hero_baixo_2.png", "hero_baixo_3.png");
        animCima = carregarAnimacao("hero_cima_0.png", "hero_cima_1.png", "hero_cima_2.png", "hero_cima_3.png");
        animEsquerda = carregarAnimacao("hero_esq_0.png", "hero_esq_1.png", "hero_esq_2.png", "hero_esq_3.png");
        animDireita = carregarAnimacao("hero_dir_0.png", "hero_dir_1.png", "hero_dir_2.png", "hero_dir_3.png");
        animAtaque = carregarAnimacao("hero_ataque_0.png", "hero_ataque_1.png"); 
        animMorrendo = carregarAnimacao("hero_morte_0.png", "hero_morte_1.png", "hero_morte_2.png");
        this.iFrames = animParado;
    }

    // verifica psicao
    public boolean setPosicao(int linha, int coluna){
        if (isMorto) return false;
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }
    
    // animacoes de direcao
    @Override
    public boolean moveUp() {
        this.orientacao = 0; 
        return super.moveUp(); 
    }

    @Override
    public boolean moveDown() {
        this.orientacao = 1; 
        return super.moveDown(); 
    }

    @Override
    public boolean moveLeft() {
        this.orientacao = 2; 
        return super.moveLeft(); 
    } 

    @Override
    public boolean moveRight() {
        this.orientacao = 3;
        return super.moveRight();
    }
    
    public int getOrientacao() {
        return this.orientacao;
    }
    
    public void atacar() {
        this.estadoAtual = Estado.ATACANDO;
        this.iFrameAtual = 0;
    }
    
    public void morrer() {
        this.estadoAtual = Estado.MORRENDO;
        this.isMorto = true; 
        this.iFrameAtual = 0; 
    }
    
    // Tira o herói do estado de morte, ele é chamado ao carregar um jogo salvo
    public void reviver() {
        this.isMorto = false;
        this.estadoAtual = Estado.PARADO;
        this.iFrameAtual = 0;
    }
    
    
    // Recarrega as animações após a deserialização 
        public void recarregarAnimacoes(String prefixo) {
        String nomeBase = prefixo.replace(".png", "");        
        animParado = carregarAnimacao(nomeBase + "_parado_0.png", nomeBase + "_parado_1.png"); 
        animBaixo = carregarAnimacao(nomeBase + "_baixo_0.png", nomeBase + "_baixo_1.png", nomeBase + "_baixo_2.png", nomeBase + "_baixo_3.png");
        animCima = carregarAnimacao(nomeBase + "_cima_0.png", nomeBase + "_cima_1.png", nomeBase + "_cima_2.png", nomeBase + "_cima_3.png");
        animEsquerda = carregarAnimacao(nomeBase + "_esq_0.png", nomeBase + "_esq_1.png", nomeBase + "_esq_2.png", nomeBase + "_esq_3.png");
        animDireita = carregarAnimacao(nomeBase + "_dir_0.png", nomeBase + "_dir_1.png", nomeBase + "_dir_2.png", nomeBase + "_dir_3.png");
        animAtaque = carregarAnimacao(nomeBase + "_ataque_0.png", nomeBase + "_1.png"); 
        animMorrendo = carregarAnimacao(nomeBase + "_morte_0.png", nomeBase + "_morte_1.png", nomeBase + "_morte_2.png");
        this.iFrames = animParado; 
    }
}