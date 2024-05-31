package tech.leonam.achaprecopro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nomeDoProduto;
    private String descricaoDoProduto;
    private String codBarras;
    private String price;
    private String localizacaoDaImagem;
    private LocalDateTime ultimaAlteracao;

}
