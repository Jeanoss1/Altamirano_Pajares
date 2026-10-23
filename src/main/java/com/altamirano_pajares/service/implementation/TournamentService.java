package com.altamirano_pajares.service.implementation;

import com.altamirano_pajares.exception.BusinessRuleException;
import com.altamirano_pajares.model.Tournament;
import com.altamirano_pajares.repository.IGenericRepository;
import com.altamirano_pajares.repository.ITournamentRepository;
import com.altamirano_pajares.service.ITournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentService extends GenericService<Tournament, Integer> implements ITournamentService {
    private final ITournamentRepository repo;

    @Override
    protected IGenericRepository<Tournament, Integer> getRepo() {
        return repo;
    }

    @Override
    public Tournament save(Tournament tournament) {
        tournament.setName(tournament.getName().trim());
        validateDates(tournament);
        if (repo.existsByNameIgnoreCase(tournament.getName())) {
            throw new BusinessRuleException("Ya existe un torneo con el nombre " + tournament.getName());
        }
        return super.save(tournament);
    }

    @Override
    public Tournament update(Integer id, Tournament tournament) {
        findById(id); // primero 404, luego reglas de negocio
        tournament.setName(tournament.getName().trim());
        validateDates(tournament);
        if (repo.existsByNameIgnoreCaseAndIdTournamentNot(tournament.getName(), id)) {
            throw new BusinessRuleException("Ya existe otro torneo con el nombre " + tournament.getName());
        }
        return super.update(id, tournament);
    }

    // Regla del caso: endDate posterior a startDate
    private void validateDates(Tournament tournament) {
        if (!tournament.getEndDate().isAfter(tournament.getStartDate())) {
            throw new BusinessRuleException("endDate (" + tournament.getEndDate()
                    + ") debe ser posterior a startDate (" + tournament.getStartDate() + ")");
        }
    }
}
