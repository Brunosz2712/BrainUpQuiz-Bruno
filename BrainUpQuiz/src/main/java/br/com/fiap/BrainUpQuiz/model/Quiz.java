package br.com.fiap.BrainUpQuiz.model;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Quiz {
    private List<Player> players = new CopyOnWriteArrayList<>();
}
