import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class BadIceCream extends JPanel {
    private String[] configMapa = {
            "XXXXXXXXXXXXXXXXXX",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "X                X",
            "XXXXXXXXXXXXXXXXXX"
    };

    class Bloco {
        int x;
        int y;
        int largura;
        int altura;
        Image imagem;

        int startX;
        int startY;

        Bloco(Image imagem, int x, int y, int largura, int altura) {
            this.imagem = imagem;
            this.x = x;
            this.y = y;
            this.largura = largura;
            this.altura = altura;
            this.startX = x;
            this.startY = y;
        }
    }

    private int linhaContador = 18;
    private int colunaContador = 18;
    private int tamTile = 32;
    private int boardLargura = colunaContador * tamTile;
    private int boardAltura = linhaContador * tamTile;

    private Image blocoGeloImagem;

    HashSet<Bloco> paredes;

    BadIceCream() {
        setPreferredSize(new Dimension(boardLargura, boardAltura));
        setBackground(Color.decode("#FBFFFF"));

        // --- ADD THIS DEBUG CODE ---
        java.net.URL imageUrl = getClass().getResource("iceblock.png");
        if (imageUrl == null) {
            System.out.println("--- ERROR! ---");
            System.out.println("Could not find the file: iceblock.png");
            System.out.println(
                    "Please make sure it's in the same folder as your .java files and is spelled exactly right.");
            System.out.println("--------------");
        } else {
            System.out.println("Found image at: " + imageUrl.toExternalForm());
        }
        // --- END DEBUG CODE ---

        // This line will still crash if the file isn't found,
        // but now you'll see the ERROR message above first.
        blocoGeloImagem = new ImageIcon(imageUrl).getImage();

        loadMap();
    }

    public void loadMap() {
        paredes = new HashSet<Bloco>();

        for (int l = 0; l < linhaContador; l++) {
            for (int c = 0; c < colunaContador; c++) {
                String linha = configMapa[l];
                char tileMapaChar = linha.charAt(c);

                int x = c * tamTile;
                int y = l * tamTile;

                if (tileMapaChar == 'X') {
                    Bloco parede = new Bloco(blocoGeloImagem, x, y, tamTile, tamTile);
                    paredes.add(parede);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (Bloco parede : paredes) {
            g.drawImage(parede.imagem, parede.x, parede.y, parede.largura, parede.altura, null);
        }

        g.setColor(Color.decode("#000000"));

    }
}
