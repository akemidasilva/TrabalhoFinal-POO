package Controler;

import Modelo.Chaser;
import Modelo.Personagem;
import Modelo.PersonagemAnimado;
import Modelo.Hero;
import Modelo.Fruta;
import Auxiliar.Fase;
import Auxiliar.Posicao;
import java.util.List;

public class ControleDeJogo {
    
    // imprime personagens (considerando camadas)
    public void desenhaTudo(Fase fase) {
        List<Personagem> personagens = fase.getPersonagens();
        
        // Camada 1: personagens estáticos
        for (Personagem p : personagens) {
            if (!(p instanceof PersonagemAnimado)) {
                p.autoDesenho();
            }
        }
        
        // Camada 2: personagens animados (exceto Hero)
        for (Personagem p : personagens) {
            if (p instanceof PersonagemAnimado && !(p instanceof Hero)) {
                p.autoDesenho();
            }
        }
        
        // Camada 3: Hero por cima
        for (Personagem p : personagens) {
            if (p instanceof Hero) {
                p.autoDesenho();
            }
        }
    }
    
    // colisões e interações (0=nada, 1=morte, 2=pegou fruta)
    public int processaTudo(Fase fase, Hero hero) {
        if (hero == null) return 0;
        
        List<Personagem> personagens = fase.getPersonagens();
        Personagem pIesimoPersonagem;
        int retorno = 0;
        
        for (int i = 0; i < personagens.size(); i++) {
            pIesimoPersonagem = personagens.get(i);
            
            if (pIesimoPersonagem == hero) {
                continue;
            }
            
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem instanceof Fruta) {
                    fase.remover(pIesimoPersonagem); // usando método da Fase
                    retorno = 2; 
                    i--; 
                } 
                else if (pIesimoPersonagem.isbMortal()) {
                    retorno = 1; 
                }
            }
        }
        
        // Movimento dos Chasers
        for (Personagem p : personagens) {
            if (p instanceof Chaser) {
                ((Chaser) p).computeDirection(hero.getPosicao());
            }
        }
        
        return retorno;
    }
    
    // verifica se não tem obstáculos na posição
    public boolean ehPosicaoValida(Fase fase, Posicao p) {
        List<Personagem> personagens = fase.getPersonagens();
        Personagem pIesimoPersonagem;
        
        for (int i = 0; i < personagens.size(); i++) {
            pIesimoPersonagem = personagens.get(i);
            
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        
        return true;
    }
}