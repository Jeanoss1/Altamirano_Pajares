package com.altamirano_pajares.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

// Caso 14 - Torneo (Tournament) - PEÑA PAJARES JHON PAOLO
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tournaments")
@Check(name = "CK_TOURNAMENT_DATES", constraints = "end_date > start_date")
@Check(name = "CK_TOURNAMENT_MAX_TEAMS", constraints = "max_teams BETWEEN 2 AND 64")
public class Tournament {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTournament;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String sport;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer maxTeams;
}
