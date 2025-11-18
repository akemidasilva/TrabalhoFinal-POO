package Controler;

import Modelo.*;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Auxiliar.Fase;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point; 
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor; 
import java.awt.datatransfer.Transferable; 
import java.awt.dnd.DnDConstants; 
import java.awt.dnd.DropTarget; 
import java.awt.dnd.DropTargetDropEvent; 
import java.awt.dnd.DropTargetListener; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException;
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List; 
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 
import java.util.zip.ZipOutputStream; 
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Implementa as 3 interfaces: Mouse, Teclado e Arrastar-e-Soltar
public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener, DropTargetListener {

    private Hero hero;
    private Fase faseAtual; 
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;

    // Variáveis de Estado do Jogo
    private final int VIDAS_INICIAIS = 3;
    private int vidasRestantes = VIDAS_INICIAIS;
    private int pontuacao = 0; 
    private boolean aguardandoResetFase = false;
    private final int TEMPO_FASE = 90; 
    private int tempoRestante;
    private long proximoSegundo;

    // Variáveis de Nível e Round
    private int nivelAtual = 1;
    private int roundFrutaAtual = 1;
    private ArrayList<Fruta> frutasRound1 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound2 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound3 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound4 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound5 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound6 = new ArrayList<>();
    private ArrayList<Fruta> frutasRound7 = new ArrayList<>();

    private final Set<Integer> teclasPressionadas = new HashSet<>();
    private Random random = new Random();

    // mapas dos niveis
    String[] mapaNivel1 = {
        "###############",
        "# S # 1 # 1 # #",
        "###############",
        "# 1 # 1 # 1 # #",
        "###############",
        "# 2 # 2 # 2 # #",
        "###############",
        "# 2 # 2 # 2 # #",
        "###############",
        "# 3 # 3 # 3 # #",
        "###############"
    };

    String[] mapaNivel2 = {
        "###############",
        "#S  1     Z   #",
        "#             #",
        "#   # # # #   #",
        "# 2         2 #",
        "# # #  Z  #   #",
        "# 3         3 #",
        "#   # # # #   #",
        "#       Z   1 #",
        "#             #",
        "###############"
    };

    String[] mapaNivel3 = {
        "###############",
        "#S            #",
        "# 1  E      1 #",
        "###############",
        "# 2       E   2#",
        "###############",
        "# 2       E   2#",
        "###############",
        "###############",
        "# 3     E   3 #",
        "###############"
    };

    String[] mapaNivel4 = {
        "###############",
        "#S  # 1 # 2 # #",
        "#   # V #   # #",
        "#   # 1 # 2 # #",
        "#   #   #   # #",
        "#   # 1 # 2 # #",
        "#   #   #   # #",
        "# V # 1 # 2 # #",
        "#   #   #   # #",
        "#   #   # V # #",
        "###############"
    };

    String[] mapaNivel5 = {
        "###############",
        "#S  1    C   2 #",
        "#   #  #     # #",
        "# # 1 #      2 #",
        "#   #    #   # #",
        "#   3         3 #",
        "#  #  #  #   # #",
        "#   1    C   2 #",
        "#   #   ##   # #",
        "#   3  #     3 #",
        "###############"
    };


    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);

        // Ativa o "Arrastar e Soltar" 
        new DropTarget(this, this);

        // Ajuste o VIEWPORT_LARGURA/ALTURA em Consts.java se esta linha der erro
        this.setSize(Consts.VIEWPORT_LARGURA * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.VIEWPORT_ALTURA * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        carregarFase(nivelAtual);
    }

    // interpreta mapa e cria objetos
    public void construirMapa(String[] mapa) {
        faseAtual = new Fase();
        frutasRound1.clear();
        frutasRound2.clear();
        frutasRound3.clear();
        frutasRound4.clear();
        frutasRound5.clear();
        frutasRound6.clear();
        frutasRound7.clear();

        String[] nomesFrutas = {"uva.png", "morango.png", "laranja.png",
                                "melancia.png", "abacate.png",
                                "kiwi.png", "pessego.png"};

        for (int linha = 0; linha < mapa.length; linha++) {
            String sLinha = mapa[linha];
            for (int col = 0; col < sLinha.length(); col++) {
                char tipo = sLinha.charAt(col);
                switch (tipo) {
                    case '#':
                        Gelo novoGelo = new Gelo("gelo.png", linha, col);
                        // Lógica de Gelo Indestrutível
                        if (linha == 0 || linha == (mapa.length - 1) || col == 0 || col == (sLinha.length() - 1)) {
                            novoGelo.setIndestrutivel(true);
                        }
                        this.addPersonagem(novoGelo);
                        break;
                    case 'S':
                        hero = new Hero("sorveteBaunilha.png", linha, col);
                        this.addPersonagem(hero);
                        break;
                    case 'E': // inimigo 1 (horizontal)
                        this.addPersonagem(new BichinhoVaiVemHorizontal("inimigo1.png", linha, col, 10)); // Lento
                        break;
                    case 'V': // inimigo 2 (vertical)
                        this.addPersonagem(new BichinhoVaiVemVertical("inimigo2.png", linha, col, 10)); // Lento
                        break;
                    case 'Z': // inimigo 3 (ziguezague)
                        this.addPersonagem(new ZigueZague("inimigo3.png", linha, col, 7)); // Médio
                        break;
                    case 'C': // inimigo 4 (chaser)
                         this.addPersonagem(new Chaser("inimigo4.png", linha, col)); // Velocidade padrão 7
                        break;
                    case '1':
                        frutasRound1.add(new Fruta(nomesFrutas[0], linha, col));
                        break;
                    case '2':
                        frutasRound2.add(new Fruta(nomesFrutas[1], linha, col));
                        break;
                    case '3':
                        frutasRound3.add(new Fruta(nomesFrutas[2], linha, col));
                        break;
                    case '4':
                        frutasRound4.add(new Fruta(nomesFrutas[3], linha, col));
                        break;
                    case '5':
                        frutasRound5.add(new Fruta(nomesFrutas[4], linha, col));
                        break;
                    case '6':
                        frutasRound6.add(new Fruta(nomesFrutas[5], linha, col));
                        break;
                    case '7':
                        frutasRound7.add(new Fruta(nomesFrutas[6], linha, col));
                        break;
                }
            }
        }
        
        // separa rounds das frutas
        for(Fruta f : frutasRound1) {
            this.addPersonagem(f);
        }
    }

    // carrega fase
    public void carregarFase(int nivel) {
        this.nivelAtual = nivel;
        this.roundFrutaAtual = 1;
        this.tempoRestante = TEMPO_FASE; 
        this.proximoSegundo = System.currentTimeMillis() + 1000; 
        
        switch(nivel) {
            case 1: construirMapa(mapaNivel1); break;
            case 2: construirMapa(mapaNivel2); break;
            case 3: construirMapa(mapaNivel3); break;
            case 4: construirMapa(mapaNivel4); break;
            case 5: construirMapa(mapaNivel5); break;
            default:
                mostrarTelaFinal();
                return;
        }
        
        this.atualizaCamera();
        atualizarTitulo();
    }
    
    // informações de nivel, vida e round
    private void atualizarTitulo() {
        int minutos = (tempoRestante < 0) ? 0 : tempoRestante / 60;
        int segundos = (tempoRestante < 0) ? 0 : tempoRestante % 60;
        String tempoFormatado = String.format("%d:%02d", minutos, segundos);

        this.setTitle("Bad Ice-Cream - Nível: " + nivelAtual + 
                      " / Vidas: " + vidasRestantes +  " / Pontuação: " + pontuacao +
                      " / Tempo: " + tempoFormatado);
    }
    
    // tela final
    private void mostrarTelaFinal() {
        String mensagem = "PARABÉNS! Você zerou o jogo!\n\n" +
                          "Pontuação Total: " + pontuacao + "\n\n" +
                          "Créditos:\n" +
                          "Akemi Lopes Ferreira da Silva - 8192177\n" +
                          "Mariana do Nascimento Ferreira - 15582241\n" +
                          "Suellen Teodorico dos Santos Silva - 15489275\n";
        JOptionPane.showMessageDialog(this, mensagem, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    
    // processa morte, rounds e fases
    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                // animacao de morte antes de resetar a fase
                if (aguardandoResetFase) {
                    if (hero.isAnimacaoMorteCompleta()) {
                        try {
                            if (vidasRestantes <= 0) { // Game Over
                                SwingUtilities.invokeAndWait(() -> {
                                    JOptionPane.showMessageDialog(Tela.this, "GAME OVER!\nVocê perdeu todas as vidas.\n" +
                                                 "Pontuação Final: " + pontuacao);
                                            });
                                nivelAtual = 1;
                                vidasRestantes = VIDAS_INICIAIS;
                                pontuacao = 0; // Reseta pontuação
                            } else { // Apenas morreu
                                SwingUtilities.invokeAndWait(() -> {
                                    JOptionPane.showMessageDialog(Tela.this, "Você morreu! Vidas restantes: " + vidasRestantes);
                                });
                            }
                            
                            teclasPressionadas.clear(); 
                            carregarFase(nivelAtual);
                            aguardandoResetFase = false; 
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
                
                // Lógica principal do jogo 
                synchronized (faseAtual) { 
                    if (!aguardandoResetFase) {
                        try {                     
                            if (System.currentTimeMillis() > proximoSegundo) {
                                tempoRestante--;
                                proximoSegundo = System.currentTimeMillis() + 1000; // Define o próximo segundo
                                
                                SwingUtilities.invokeLater(() -> atualizarTitulo()); 

                                if (tempoRestante <= 0) {
                                    hero.morrer(); 
                                    vidasRestantes--;
                                    aguardandoResetFase = true; 
                                }
                            }
                        // verifica se acabaram as frutas para avancar
                        if (!temFrutas()) { 
                            if (roundFrutaAtual == 1 && !frutasRound2.isEmpty()) {
                                roundFrutaAtual++;
                                for(Fruta f : frutasRound2) { addPersonagem(f); }
                                atualizarTitulo();
                            } else if (roundFrutaAtual == 2 && !frutasRound3.isEmpty()) {
                                roundFrutaAtual++;
                                for(Fruta f : frutasRound3) { addPersonagem(f); }
                                atualizarTitulo();                            
                            } else {
                                nivelAtual++;
                                SwingUtilities.invokeAndWait(() -> {
                                    JOptionPane.showMessageDialog(Tela.this, "Fase " + (nivelAtual-1) + " concluída!\n" + 
                                                                             "Pontuação: " + pontuacao);
                                });
                                teclasPressionadas.clear();
                                carregarFase(nivelAtual); 
                            }
                        }
                        
                        processarInputMovimento(); 
                    
                        if (faseAtual != null && !faseAtual.getPersonagens().isEmpty() && nivelAtual <= 5) {
                            int status = cj.processaTudo(faseAtual, Tela.this.hero);
                            
                            if (status == 1) { 
                                hero.morrer(); 
                                vidasRestantes--;
                                atualizarTitulo();
                                aguardandoResetFase = true; 
                            } else if (status == 2) { 
                                pontuacao += 10; 
                                atualizarTitulo(); 
                            }
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }
                
    
    // teclas de movimento
    private void processarInputMovimento() {
        if(hero == null || hero.isMorto()) return;
        
        boolean moveu = false;
        
        if (teclasPressionadas.contains(KeyEvent.VK_UP)) {
            moveu = hero.moveUp();
        } else if (teclasPressionadas.contains(KeyEvent.VK_DOWN)) {
            moveu = hero.moveDown();
        } else if (teclasPressionadas.contains(KeyEvent.VK_LEFT)) {
            moveu = hero.moveLeft();
        } else if (teclasPressionadas.contains(KeyEvent.VK_RIGHT)) {
            moveu = hero.moveRight();
        }
        
        if (moveu) {
            atualizaCamera();
        }
    }
    
    // busca posicao personagem
    public Personagem getPersonagemNaPosicao(Posicao p) {
        for (Personagem pIesimo : faseAtual.getPersonagens()) { 
            if (pIesimo != hero && pIesimo.getPosicao().igual(p)) {
                return pIesimo;
            }
        }
        return null;
    }
    
    // verifica frutas no mapa
    public boolean temFrutas() {
        for(Personagem p : faseAtual.getPersonagens()) {
            if (p instanceof Fruta) return true;
        }
        return false;
    }

    public Hero getHero() { return hero; }
    public void addPersonagem(Personagem umPersonagem) { faseAtual.adicionar(umPersonagem); } 
    public void removePersonagem(Personagem umPersonagem) { faseAtual.remover(umPersonagem); } 
    public Graphics getGraphicsBuffer() { return g2; }
    public int getCameraLinha() { return cameraLinha; }
    public int getCameraColuna() { return cameraColuna; }
    
    public boolean ehPosicaoValida(Posicao p) { 
        return cj.ehPosicaoValida(faseAtual, p); 
    }

    // frames 
    public void paint(Graphics gOld) {
        if (this.getBufferStrategy() == null) return;
        
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        
        for (int i = 0; i < Consts.VIEWPORT_ALTURA; i++) {
            for (int j = 0; j < Consts.VIEWPORT_LARGURA; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;
                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "bricks.png");
                        g2.drawImage(newImage, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {}
                }
            }
        }
        
        if (this.faseAtual != null && !this.faseAtual.getPersonagens().isEmpty()) { 
            this.cj.desenhaTudo(faseAtual); 
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    // manter camera fixa (ou pode implementar a lógica de seguir)
    private void atualizaCamera() {
        // Lógica para câmera seguir o herói (se Consts.VIEWPORT for menor que MUNDO)
        // int linha = hero.getPosicao().getLinha();
        // int coluna = hero.getPosicao().getColuna();
        // cameraLinha = Math.max(0, Math.min(linha - Consts.VIEWPORT_ALTURA / 2, Consts.MUNDO_ALTURA - Consts.VIEWPORT_ALTURA));
        // cameraColuna = Math.max(0, Math.min(coluna - Consts.VIEWPORT_LARGURA / 2, Consts.MUNDO_LARGURA - Consts.VIEWPORT_LARGURA));
        
        // Câmera fixa (se VIEWPORT == MUNDO)
         cameraLinha = 0;
         cameraColuna = 0;
    }

    // teclas pressionadas
    public void keyPressed(KeyEvent e) {
        try {
            if (aguardandoResetFase) return; 
            if (teclasPressionadas.contains(e.getKeyCode())) {
                 // Permite que teclas de ação (S, L, K, J, H) funcionem
                 if (e.getKeyCode() != KeyEvent.VK_S && e.getKeyCode() != KeyEvent.VK_L && 
                     e.getKeyCode() != KeyEvent.VK_K && e.getKeyCode() != KeyEvent.VK_J && 
                     e.getKeyCode() != KeyEvent.VK_H) {
                    return;
                 }
            }
            
            teclasPressionadas.add(e.getKeyCode());

            // Permite Carregar (L) mesmo se o herói estiver morto
            if(hero == null || hero.isMorto()) {
                 if (e.getKeyCode() != KeyEvent.VK_L) {
                    return;
                 }
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                hero.atacar(); 

                int lin = hero.getPosicao().getLinha();
                int col = hero.getPosicao().getColuna();
                switch(hero.getOrientacao()) {
                    case 0: lin--; break;
                    case 1: lin++; break;
                    case 2: col--; break;
                    case 3: col++; break;
                }

                if (lin <= 0 || lin >= (Consts.MUNDO_ALTURA - 1) || col <= 0 || col >= (Consts.MUNDO_LARGURA - 1)) {
                    return; 
                }

                Personagem alvo = getPersonagemNaPosicao(new Posicao(lin, col));

                if (alvo != null && alvo instanceof Gelo) {
                    if (!((Gelo)alvo).isIndestrutivel()) {
                        faseAtual.remover(alvo); 
                    }
                } else if (alvo == null) {
                    if(lin >= 0 && lin < Consts.MUNDO_ALTURA && col >= 0 && col < Consts.MUNDO_LARGURA)
                        faseAtual.adicionar(new Gelo("gelo.png", lin, col)); 
                }
            }
            // Lógica de salvar e carregar
            else if (e.getKeyCode() == KeyEvent.VK_S) {
                File file = new File("POO.dat");
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                
                out.writeObject(new ArrayList<>(faseAtual.getPersonagens())); 
                out.writeObject(nivelAtual);
                out.writeObject(vidasRestantes);
                out.writeObject(pontuacao);
                out.writeObject(tempoRestante);
                out.close();
                JOptionPane.showMessageDialog(this, "Jogo salvo!");

            } else if (e.getKeyCode() == KeyEvent.VK_L) {
                File file = new File("POO.dat");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this, "Nenhum jogo salvo (POO.dat) foi encontrado!");
                    return;
                }

                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                
                ArrayList<Personagem> tempLista = (ArrayList<Personagem>) in.readObject(); 
                faseAtual = new Fase(); 
                for(Personagem p : tempLista) { 
                    faseAtual.adicionar(p);
                }
                nivelAtual = (int) in.readObject();
                vidasRestantes = (int) in.readObject();
                pontuacao = (int) in.readObject();
                tempoRestante = (int) in.readObject();
                in.close();

                this.hero = null; 
                for(Personagem p : faseAtual.getPersonagens()) {
                    if(p instanceof Hero) {
                        this.hero = (Hero)p;
                        break;
                    }
                }
                
                if(this.hero != null) {
                    this.hero.reviver();
                    this.aguardandoResetFase = false;
                }

                proximoSegundo = System.currentTimeMillis() + 1000;
                teclasPressionadas.clear(); 
                atualizarTitulo(); 
                atualizaCamera(); 
                JOptionPane.showMessageDialog(this, "Jogo carregado!");
            }
            
            // Criação dos ZIPS
            else if (e.getKeyCode() == KeyEvent.VK_K) { // Cria Inimigo Vertical
                criarZipPersonagem(new BichinhoVaiVemVertical("inimigo2.png", 0, 0, 10), "InimigoVertical.zip");
            }            
            else if (e.getKeyCode() == KeyEvent.VK_J) { // Cria Chaser
                criarZipPersonagem(new Chaser("inimigo4.png", 0, 0), "InimigoChaser.zip");
            }
            else if (e.getKeyCode() == KeyEvent.VK_H) { // Cria Horizontal
                criarZipPersonagem(new BichinhoVaiVemHorizontal("inimigo1.png", 0, 0, 10), "InimigoHorizontal.zip");
            }
            
        } catch (Exception ee) { 
            ee.printStackTrace(); 
            if (ee instanceof java.io.FileNotFoundException) {
                JOptionPane.showMessageDialog(this, "Erro: Arquivo 'POO.dat' não encontrado.");
            } else if (ee instanceof ClassNotFoundException) {
                JOptionPane.showMessageDialog(this, "Erro: O arquivo de save está corrompido ou é de uma versão antiga.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar/carregar: " + ee.getMessage());
            }
        }
    }
    
    private void criarZipPersonagem(Personagem p, String nomeArquivoZip) {
        try {
            String datFileName = "personagem_temp.dat";
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(datFileName));
            out.writeObject(p);
            out.close();
            
            FileOutputStream fos = new FileOutputStream(nomeArquivoZip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry(datFileName)); 
            
            FileInputStream fis = new FileInputStream(datFileName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close();
            
            zos.closeEntry();
            zos.close();
            
            new File(datFileName).delete(); 
            
            JOptionPane.showMessageDialog(this, "Arquivo '" + nomeArquivoZip + "' criado na pasta do projeto!");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao criar ZIP: " + ex.getMessage());
        }
    }


    // remove tecla solta
    public void keyReleased(KeyEvent e) { teclasPressionadas.remove(e.getKeyCode()); }  
    
    public void mousePressed(MouseEvent e) {
    }
    
    // config tela
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bad Ice-Cream - POO");
        setAlwaysOnTop(true);
        setResizable(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 525, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 385, Short.MAX_VALUE));
        pack();
    }
    
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}

    // Método principal que é chamado quando o usuário solta o arquivo
    @Override
    public synchronized void drop(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable transferable = dtde.getTransferable();
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (!files.isEmpty()) {
                    File droppedFile = files.get(0);
                    Point dropPoint = dtde.getLocation();
                    carregarObjetoDoZip(droppedFile, dropPoint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Método para ler o Zip e adicionar o personagem
    private void carregarObjetoDoZip(File zipFile, Point dropPoint) {
        new Thread(() -> {
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
                ZipEntry entry = zis.getNextEntry(); 
                if (entry != null && !entry.isDirectory()) {
                    ObjectInputStream ois = new ObjectInputStream(zis);
                    Personagem p = (Personagem) ois.readObject();
                    
                    if (p instanceof PersonagemAnimado pa) {
                        String nomeBase = pa.getNomeImagem(); 
                        pa.recarregarAnimacoes(nomeBase); 
                    }
                    
                    int insetsTop = getInsets().top;
                    int insetsLeft = getInsets().left;
                    
                    int col = ((dropPoint.x - insetsLeft) / Consts.CELL_SIDE) + cameraColuna;
                    int lin = ((dropPoint.y - insetsTop) / Consts.CELL_SIDE) + cameraLinha;

                    SwingUtilities.invokeLater(() -> {
                        p.setPosicao(lin, col);
                        addPersonagem(p);
                        repaint(); 
                    });
                }
                zis.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Tela.this, "Erro ao carregar o arquivo ZIP: " + e.getMessage());
                });
            }
        }).start();
    }
    @Override
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {}
    @Override
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {}
    @Override
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {}
    @Override
    public void dragExit(java.awt.dnd.DropTargetEvent dte) {}
}