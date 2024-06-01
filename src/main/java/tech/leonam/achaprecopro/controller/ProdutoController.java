package tech.leonam.achaprecopro.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
    @Value("${diretorio}")
    private String UPLOAD_DIR;

    @Autowired
    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

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
    public ResponseEntity<ProdutoEntity> alter(@RequestPart("produto") @Valid ProdutoSaveDTO dto,
                                               @RequestPart(value = "imagem") MultipartFile imagem,
                                               @PathVariable Long id) throws IOException {
        return ResponseEntity.ok().body(service.alteraComImagem(dto, id, imagem));
    }

    @GetMapping("/imagem/{nomeDoArquivo}")
    @ResponseBody
    public ResponseEntity<Resource> baixaImagem(@PathVariable String nomeDoArquivo){
        var arquivo = new FileSystemResource(UPLOAD_DIR + nomeDoArquivo);

        if (!arquivo.exists()) {
            return ResponseEntity.notFound().build();
        }
        String extensao = "";
        int dotIndex = nomeDoArquivo.lastIndexOf(".");
        if (dotIndex >= 0) {
            extensao = nomeDoArquivo.substring(dotIndex + 1);
        }
        var httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=foto_do_produto." + extensao);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(arquivo);
    }

}
