package br.com.fiap.BrainUpQuiz.controller;

import br.com.fiap.BrainUpQuiz.model.Player;
import br.com.fiap.BrainUpQuiz.repository.QuizRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizRepository repository;
    private final List<SseEmitter> adminEmitters = new CopyOnWriteArrayList<>();

    public QuizController(QuizRepository repository) {
        this.repository = repository;
    }

    // POST /quiz/start
    @PostMapping("/start")
    public ResponseEntity<Player> start(@RequestBody Player request) {
        Player player = repository.addPlayer(request.getName());

        // Notifica o admin
        for (SseEmitter emitter : adminEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("player.joined")
                        .data(player));
            } catch (Exception e) {
                adminEmitters.remove(emitter);
            }
        }

        return ResponseEntity.ok(player);
    }

    // POST /quiz/exit
    @PostMapping("/exit")
    public ResponseEntity<Player> exit(@RequestBody Player request) {
        Player player = repository.removePlayer(request.getId());

        if (player != null) {
            // Notifica o admin
            for (SseEmitter emitter : adminEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("player.exited")
                            .data(player));
                } catch (Exception e) {
                    adminEmitters.remove(emitter);
                }
            }
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /quiz/stream/admin user
    @GetMapping("/stream/admin")
    public SseEmitter streamAdmin() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        adminEmitters.add(emitter);
        emitter.onCompletion(() -> adminEmitters.remove(emitter));
        emitter.onTimeout(() -> adminEmitters.remove(emitter));
        return emitter;
    }
}
