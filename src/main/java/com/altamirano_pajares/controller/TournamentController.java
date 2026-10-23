package com.altamirano_pajares.controller;

import com.altamirano_pajares.dto.TournamentDTO;
import com.altamirano_pajares.model.Tournament;
import com.altamirano_pajares.service.ITournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final ITournamentService service;
    private final ModelMapper modelMapper;

    @GetMapping // 200 ok
    public ResponseEntity<List<TournamentDTO>> findAll() {
        List<TournamentDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}") // 200 ok | 404 not found
    public ResponseEntity<TournamentDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(convertToDto(service.findById(id)));
    }

    @PostMapping // 201 created | 400 bad request
    public ResponseEntity<TournamentDTO> save(@Valid @RequestBody TournamentDTO dto) {
        Tournament obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getIdTournament()).toUri();
        return ResponseEntity.created(location).body(convertToDto(obj));
    }

    @PutMapping("/{id}") // 200 ok | 400 bad request | 404 not found
    public ResponseEntity<TournamentDTO> update(@PathVariable Integer id, @Valid @RequestBody TournamentDTO dto) {
        Tournament obj = service.update(id, convertToEntity(dto));
        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}") // 204 no content | 404 not found
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TournamentDTO convertToDto(Tournament obj) {
        return modelMapper.map(obj, TournamentDTO.class);
    }

    private Tournament convertToEntity(TournamentDTO dto) {
        return modelMapper.map(dto, Tournament.class);
    }
}
