package Auxiliar;

import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import Controler.Tela;

public class Desenho implements Serializable {
    static Tela jCenario;

    // referencias para tela do jogo
    public static void setCenario(Tela umJCenario) {
        jCenario = umJCenario;
    }

    
    public static Tela acessoATelaDoJogo() {
        return jCenario;
    }

    public static Graphics getGraphicsDaTela() {
        return jCenario.getGraphicsBuffer();
    }

    // imprime imagem na posicao
    public static void desenhar(ImageIcon iImage, int iColuna, int iLinha) {
        // coord para viewport
        int telaX = (iColuna - jCenario.getCameraColuna()) * Consts.CELL_SIDE;
        int telaY = (iLinha - jCenario.getCameraLinha()) * Consts.CELL_SIDE;

        // so desenha se estiver deentro da janela
        if (telaX >= 0 && telaX < Consts.VIEWPORT_LARGURA * Consts.CELL_SIDE
                && telaY >= 0 && telaY < Consts.VIEWPORT_ALTURA * Consts.CELL_SIDE) {
            iImage.paintIcon(jCenario, getGraphicsDaTela(), telaX, telaY);
        }
    }

}
