import Controler.Tela;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        
        // tela de início
        String mensagem =
"╔════════════════════════════════╗\n" +
"║        BAD ICE CREAM ❄️                          ║\n" +
"╠════════════════════════════════╣\n" +
"║   Controles:                                              ║\n" +
"║   ↑ ↓ ← →  Movimentação                       ║\n" +
"║   Espaço  Criar/Quebrar Gelo               ║\n" +
"║   S       Salvar Jogo                                 ║\n" +
"║   L       Carregar Jogo                             ║\n" +
"║                                                                   ║\n" +
"║ Objetivo:                                                  ║\n" +
"║ Colete todas as frutas para                   ║\n" +
"║ avançar de fase!                                     ║\n" +
"╠════════════════════════════════╣\n" +
"║   Pressione OK para começar!              ║\n" +
"╚════════════════════════════════╝";


        JOptionPane.showMessageDialog(null, mensagem, "Bad-Ice-Cream", JOptionPane.INFORMATION_MESSAGE);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tela tTela = new Tela();
                tTela.setVisible(true);
                tTela.createBufferStrategy(2);
                tTela.go();
            }

            
        });
    }
}