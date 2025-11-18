package Modelo;

public class BichinhoVaiVemHorizontal extends PersonagemAnimado {

    private boolean bRight;
    private int iContador;
    private int velocidade; 

    // inimigo1 move esq/dir
    public BichinhoVaiVemHorizontal(String sNomeImagePNG, int linha, int coluna, int velocidade) {
        super(sNomeImagePNG, linha, coluna);         
        bRight = true;
        iContador = 0;
        this.velocidade = velocidade;
        this.bTransponivel = true;
        this.bMortal = true;        
        
        String prefixo = sNomeImagePNG.replace(".png", ""); 
        
        animEsquerda = carregarAnimacao(prefixo + "_esq_0.png", prefixo + "_esq_1.png");
        animDireita = carregarAnimacao(prefixo + "_dir_0.png", prefixo + "_dir_1.png");
        
        this.iFrames = animDireita;
        this.animParado = animDireita; 
    }

    // muda direcao ao colidir
    public void autoDesenho() {
        iContador++;
        if (iContador == this.velocidade) { 
            iContador = 0;
            if (bRight) {
                if(!this.moveRight()) bRight = false; 
            } else {
                if(!this.moveLeft()) bRight = true; 
            }
        }
        
        // animacao
        if (bRight) {
            this.estadoAtual = Estado.DIREITA;
            this.animParado = animDireita;
        } else {
            this.estadoAtual = Estado.ESQUERDA;
            this.animParado = animEsquerda; 
        }
        
        super.autoDesenho();
    }
    
    @Override
    public void recarregarAnimacoes(String prefixo) {
        String nomeBase = prefixo.replace(".png", ""); 
        animEsquerda = carregarAnimacao(nomeBase + "_esq_0.png", nomeBase + "_esq_1.png");
        animDireita = carregarAnimacao(nomeBase + "_dir_0.png", nomeBase + "_dir_1.png");        
        this.iFrames = animDireita; 
        this.animParado = animDireita; 
    }
}