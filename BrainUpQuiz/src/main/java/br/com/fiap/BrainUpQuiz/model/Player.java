package br.com.fiap.BrainUpQuiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private UUID id;
    private String name;
    private boolean active;
}
