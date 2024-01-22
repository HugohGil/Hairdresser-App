package com.gps.shared_resources;

import java.io.Serializable;

public class TypeService implements Serializable {
    private int id;
    private String nome;
    private int duracao;

    public TypeService(int id, String nome, int duracao) {
        this.id = id;
        this.nome = nome;
        this.duracao = duracao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getDuracao() {
        return duracao;
    }

    @Override
    public String toString() {
        return "Tipo: " + nome + "\n" +
                "Duração: " + duracao + ":00 h";
    }
}
