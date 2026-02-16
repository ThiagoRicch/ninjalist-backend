package com.example.ninjalist.model;

import com.example.ninjalist.prioridades.PrioridadesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class TaskModel {
    @Id
    @GeneratedValue
    private Long id;
    private String titulo;
    private String descricao;
    private boolean ativo = true;
    private PrioridadesEnum prioridade;
    private LocalDate datatask = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    public TaskModel() {
        this.prioridade = PrioridadesEnum.LOW;
    }

}
