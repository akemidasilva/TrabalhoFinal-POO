package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

public class Chaser extends PersonagemAnimado {

    private boolean iDirectionV; 
    private boolean iDirectionH; 
    private int counter;
    private int velocidade; 
    private Personagem alvoAtaque = null; 
    
    // inimigo2 move todas direcoes e quebra gelo
    public Chaser(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna); 
        iDirectionV = true;
        iDirectionH = true;
        this.bTransponivel = true;
        this.bMortal = true;
        counter = 0;
        this.velocidade = 7; 
        
        String prefixo = sNomeImagePNG.replace(".png", "");
        animCima = carregarAnimacao(prefixo + "_cima_0.png", prefixo + "_cima_1.png");
        animBaixo = carregarAnimacao(prefixo + "_baixo_0.png", prefixo + "_baixo_1.png");
        animEsquerda = carregarAnimacao(prefixo + "_esq_0.png", prefixo + "_esq_1.png");
        animDireita = carregarAnimacao(prefixo + "_dir_0.png", prefixo + "_dir_1.png");
        
        animAtaque = carregarAnimacao(
            prefixo + "_ataque_0.png", prefixo + "_ataque_1.png", prefixo + "_ataque_2.png",
            prefixo + "_ataque_3.png", prefixo + "_ataque_4.png", prefixo + "_ataque_5.png",
            prefixo + "_ataque_6.png"
        );
        
        this.iFrames = animDireita;
        this.animParado = animDireita;
    }

    public void computeDirection(Posicao heroPos) {
        if (estadoAtual == Estado.ATACANDO) return; 
        
        if (heroPos.getColuna() < this.getPosicao().getColuna()) iDirectionH = true;
        else if (heroPos.getColuna() > this.getPosicao().getColuna()) iDirectionH = false;
        
        if (heroPos.getLinha() < this.getPosicao().getLinha()) iDirectionV = true;
        else if (heroPos.getLinha() > this.getPosicao().getLinha()) iDirectionV = false;
    }
    
    // quebra gelo
    private boolean tentarQuebrarGelo(Posicao pAlvo) {
        Personagem alvo = Desenho.acessoATelaDoJogo().getPersonagemNaPosicao(pAlvo);
        
        if (alvo != null && alvo instanceof Gelo && !((Gelo)alvo).isIndestrutivel()) {
            this.estadoAtual = Estado.ATACANDO;
            this.iFrameAtual = 0; 
            this.alvoAtaque = alvo; 
            return true; 
        }
        return false; 
    }
    
    @Override
    public boolean moveUp() {
        if (super.moveUp()) return true; 
        Posicao pAlvo = this.getPosicao().clonar(); pAlvo.moveUp();
        return tentarQuebrarGelo(pAlvo);
    }
    @Override
    public boolean moveDown() {
        if (super.moveDown()) return true;
        Posicao pAlvo = this.getPosicao().clonar(); pAlvo.moveDown();
        return tentarQuebrarGelo(pAlvo);
    }
    @Override
    public boolean moveLeft() {
        if (super.moveLeft()) return true;
        Posicao pAlvo = this.getPosicao().clonar(); pAlvo.moveLeft();
        return tentarQuebrarGelo(pAlvo);
    }
    @Override
    public boolean moveRight() {
        if (super.moveRight()) return true;
        Posicao pAlvo = this.getPosicao().clonar(); pAlvo.moveRight();
        return tentarQuebrarGelo(pAlvo);
    }

    // muda direcao ao colidir

    @Override
    public void autoDesenho() {
        if (estadoAtual == Estado.ATACANDO) {
            super.autoDesenho(); 
            
            if (estadoAtual == Estado.PARADO && alvoAtaque != null) {
                Desenho.acessoATelaDoJogo().removePersonagem(alvoAtaque); 
                alvoAtaque = null; 
            }
            return; 
        }

        counter++;
        if (counter == this.velocidade) { 
            counter = 0;
            
            Posicao heroPos = Desenho.acessoATelaDoJogo().getHero().getPosicao();
            int deltaLinha = Math.abs(heroPos.getLinha() - this.getPosicao().getLinha());
            int deltaColuna = Math.abs(heroPos.getColuna() - this.getPosicao().getColuna());

            boolean moveu = false;
            if(deltaLinha > deltaColuna) { 
                if (iDirectionV) { moveu = this.moveUp(); } else { moveu = this.moveDown(); }
                if (!moveu) { 
                    if (iDirectionH) { this.moveLeft(); } else { this.moveRight(); }
                }
            } else { 
                if (iDirectionH) { moveu = this.moveLeft(); } else { moveu = this.moveRight(); }
                if (!moveu) { 
                    if (iDirectionV) { this.moveUp(); } else { this.moveDown(); }
                }
            }
        }
        
        if (iDirectionH) {
            this.estadoAtual = Estado.ESQUERDA;
            this.animParado = animEsquerda;
        } else {
            this.estadoAtual = Estado.DIREITA;
            this.animParado = animDireita;
        }
        if (iDirectionV) {
            this.estadoAtual = Estado.CIMA;
            this.animParado = animCima;
        } else {
            this.estadoAtual = Estado.BAIXO;
            this.animParado = animBaixo;
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
        animAtaque = carregarAnimacao(nomeBase + "_ataque_0.png", nomeBase + "_ataque_1.png", nomeBase + "_ataque_2.png",
            nomeBase + "_ataque_3.png", nomeBase + "_ataque_4.png", nomeBase + "_ataque_5.png",
            nomeBase + "_ataque_6.png"
        );
        this.iFrames = animDireita;
        this.animParado = animDireita;
    }
}