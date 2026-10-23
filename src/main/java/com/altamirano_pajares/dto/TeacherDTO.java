package com.altamirano_pajares.dto;

import com.altamirano_pajares.model.AcademicDegree;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // el id lo genera la BD, el cliente no lo envia
    private Integer idTeacher;

    @NotBlank(message = "teacherCode es obligatorio")
    @Size(max = 20, message = "teacherCode admite como maximo 20 caracteres")
    private String teacherCode;

    @NotBlank(message = "firstName es obligatorio")
    @Size(max = 60, message = "firstName admite como maximo 60 caracteres")
    private String firstName;

    @NotBlank(message = "lastName es obligatorio")
    @Size(max = 60, message = "lastName admite como maximo 60 caracteres")
    private String lastName;

    @NotBlank(message = "email es obligatorio")
    @Email(regexp = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$", message = "email no tiene un formato valido")
    @Size(max = 100, message = "email admite como maximo 100 caracteres")
    private String email;

    @NotNull(message = "academicDegree es obligatorio (BACHILLER, MAGISTER, DOCTOR)")
    private AcademicDegree academicDegree;
}
