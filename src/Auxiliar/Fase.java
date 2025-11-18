package Auxiliar;

import Modelo.Personagem;
import java.util.ArrayList;
import java.util.List;

public class Fase {

    private List<Personagem> personagens = new ArrayList<>();

    public Fase() { }

    public void adicionar(Personagem p) {
        personagens.add(p);
    }

    public void remover(Personagem p) {
        personagens.remove(p);
    }

    public List<Personagem> getPersonagens() {
        return personagens;
    }
}