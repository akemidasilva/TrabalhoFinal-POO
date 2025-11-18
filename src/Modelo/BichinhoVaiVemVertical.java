package Modelo;


public class BichinhoVaiVemVertical extends PersonagemAnimado {
    
    private boolean bUp;
    private int contadorDeFrames;
    private int velocidade; 

    // inimigo2 move cima/baixo
    public BichinhoVaiVemVertical(String sNomeImagePNG, int linha, int coluna, int velocidade) {
        super(sNomeImagePNG, linha, coluna);         
        contadorDeFrames = 0;
        this.velocidade = velocidade;
        this.bTransponivel = true;        
        this.bMortal = true;
        bUp = true;
        
        String prefixo = sNomeImagePNG.replace(".png", ""); 
        
        animCima = carregarAnimacao(prefixo + "_cima_0.png", prefixo + "_cima_1.png");
        animBaixo = carregarAnimacao(prefixo + "_baixo_0.png", prefixo + "_baixo_1.png");
        
        this.iFrames = animBaixo;
        this.animParado = animBaixo;
    }

    // muda direcao ao colidir
    public void autoDesenho(){
        contadorDeFrames++;
        if(contadorDeFrames == this.velocidade){
            contadorDeFrames = 0;
            if(bUp) {
                if(!this.moveUp()) bUp = false; 
            } else {
                if(!this.moveDown()) bUp = true; 
            }
        }
        
        // animacao
        if (bUp) {
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
        this.iFrames = animBaixo; 
        this.animParado = animBaixo; 
    }
}