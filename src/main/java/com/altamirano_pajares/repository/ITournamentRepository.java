package com.altamirano_pajares.repository;

import com.altamirano_pajares.model.Tournament;

public interface ITournamentRepository extends IGenericRepository<Tournament, Integer> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdTournamentNot(String name, Integer idTournament);
}
