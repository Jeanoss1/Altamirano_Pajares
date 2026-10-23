package com.altamirano_pajares.controller;

import com.altamirano_pajares.dto.TeacherDTO;
import com.altamirano_pajares.model.Teacher;
import com.altamirano_pajares.service.ITeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final ITeacherService service;
    private final ModelMapper modelMapper;

    @GetMapping // 200 ok
    public ResponseEntity<List<TeacherDTO>> findAll() {
        List<TeacherDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}") // 200 ok | 404 not found
    public ResponseEntity<TeacherDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(convertToDto(service.findById(id)));
    }

    @PostMapping // 201 created | 400 bad request
    public ResponseEntity<TeacherDTO> save(@Valid @RequestBody TeacherDTO dto) {
        Teacher obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getIdTeacher()).toUri();
        return ResponseEntity.created(location).body(convertToDto(obj));
    }

    @PutMapping("/{id}") // 200 ok | 400 bad request | 404 not found
    public ResponseEntity<TeacherDTO> update(@PathVariable Integer id, @Valid @RequestBody TeacherDTO dto) {
        Teacher obj = service.update(id, convertToEntity(dto));
        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}") // 204 no content | 404 not found
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeacherDTO convertToDto(Teacher obj) {
        return modelMapper.map(obj, TeacherDTO.class);
    }

    private Teacher convertToEntity(TeacherDTO dto) {
        return modelMapper.map(dto, Teacher.class);
    }
}
