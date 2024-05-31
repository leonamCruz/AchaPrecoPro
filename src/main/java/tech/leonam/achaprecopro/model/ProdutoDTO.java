package tech.leonam.achaprecopro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProdutoDTO {
    @NotBlank
    private String nomeDoProduto;
    @NotBlank
    private String descricaoDoProduto;
    @NotBlank
    private String codBarras;
    @NotBlank
    private String price;
    @NotBlank
    private String localizacaoDaImagem;
}
