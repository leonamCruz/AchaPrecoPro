package tech.leonam.achaprecopro.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
