package br.com.ifsul.cadrasto.usuarios.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static java.lang.String.format;

@Data
@Builder
@AllArgsConstructor
public final class Usuario implements Serializable {

    private static final long serialVersionUID = -7800660453018639622L;

    private final String nome;
    private final String email;
    private final String telefone;
    private final LocalDate dataNascimento;
    private final Genero genero;
    private final List<String> interesses;

    @Override
    public String toString() {
        return format("%s:%s", nome, genero);
    }
}
