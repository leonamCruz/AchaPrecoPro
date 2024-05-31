package tech.leonam.achaprecopro.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.leonam.achaprecopro.model.ProdutoDTO;
import tech.leonam.achaprecopro.model.ProdutoEntity;
import tech.leonam.achaprecopro.model.ProdutoSaveDTO;
import tech.leonam.achaprecopro.service.ProdutoService;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/produto")
public class ProdutoController {

    private ProdutoService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoEntity> save(@RequestPart("produto") @Valid ProdutoSaveDTO dto,
                                              @RequestPart("imagem") MultipartFile imagem) throws IOException {

        var entitySaved = service.save(dto, imagem);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entitySaved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(entitySaved);
    }


    @GetMapping
    public ResponseEntity<List<ProdutoEntity>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoEntity> getCodBarras(@PathVariable String codigo) {
        return ResponseEntity.ok().body(service.getByCodigoDeBarras(codigo));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoEntity> alter(@RequestPart("produto") @Valid ProdutoDTO dto,
                                               @RequestPart(value = "imagem") MultipartFile imagem,
                                               @PathVariable Long id) throws IOException {
        return ResponseEntity.ok().body(service.save(dto, id, imagem));
    }

}
