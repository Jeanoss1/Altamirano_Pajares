package com.altamirano_pajares.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // el id lo genera la BD, el cliente no lo envia
    private Integer idTournament;

    @NotBlank(message = "name es obligatorio")
    @Size(max = 100, message = "name admite como maximo 100 caracteres")
    private String name;

    @NotBlank(message = "sport es obligatorio")
    @Size(max = 50, message = "sport admite como maximo 50 caracteres")
    private String sport;

    @NotNull(message = "startDate es obligatorio (yyyy-MM-dd)")
    private LocalDate startDate;

    @NotNull(message = "endDate es obligatorio (yyyy-MM-dd)")
    private LocalDate endDate; // la regla endDate > startDate se valida en el servicio

    @NotNull(message = "maxTeams es obligatorio")
    @Min(value = 2, message = "maxTeams debe ser como minimo 2")
    @Max(value = 64, message = "maxTeams debe ser como maximo 64")
    private Integer maxTeams;
}
