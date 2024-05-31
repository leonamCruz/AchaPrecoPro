package tech.leonam.achaprecopro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoSaveDTO {
    @NotBlank
    private String nomeDoProduto;
    @NotBlank
    private String descricaoDoProduto;
    @NotBlank
    private String codBarras;
    @NotBlank
    @Positive
    private String preco;
}
