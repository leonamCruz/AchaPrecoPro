package tech.leonam.achaprecopro.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.leonam.achaprecopro.model.ProdutoDTO;
import tech.leonam.achaprecopro.model.ProdutoEntity;
import tech.leonam.achaprecopro.service.ProdutoService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/produto")
public class ProdutoController {

    private ProdutoService service;

    @PostMapping
    public ResponseEntity<ProdutoEntity> save(@RequestBody @Valid ProdutoDTO dto) {
        var entitySaved = service.save(dto);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entitySaved.getId()).toUri();

        return ResponseEntity.created(uri).body(entitySaved);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoEntity>> getAll(){
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoEntity> getCodBarras(@PathVariable String codigo){
        return ResponseEntity.ok().body(service.getByCodigoDeBarras(codigo));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoEntity> alter(@RequestBody @Valid ProdutoDTO dto, @PathVariable Long id){
        return ResponseEntity.ok().body(service.save(dto, id));
    }

}
