package tech.leonam.achaprecopro.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.leonam.achaprecopro.model.ProdutoDTO;
import tech.leonam.achaprecopro.model.ProdutoEntity;
import tech.leonam.achaprecopro.model.ProdutoSaveDTO;
import tech.leonam.achaprecopro.repository.ProdutoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProdutoService {

    private ProdutoRepository repository;
    private ModelMapper modelMapper;
    private static final String UPLOAD_DIR = "imagem_dos_produtos/";

    public ProdutoEntity save(ProdutoSaveDTO dto, MultipartFile imagem) throws IOException {
        var converted = modelMapper.map(dto, ProdutoEntity.class);
        converted.setUltimaAlteracao(LocalDateTime.now());

        String imagePath = saveImage(imagem);
        converted.setLocalizacaoDaImagem(imagePath);

        return repository.save(converted);
    }

    public ProdutoEntity save(ProdutoDTO dto, Long id, MultipartFile imagem) throws IOException {
        var converted = modelMapper.map(dto, ProdutoEntity.class);
        String imagePath = saveImage(imagem);

        converted.setId(id);
        converted.setLocalizacaoDaImagem(imagePath);
        converted.setUltimaAlteracao(LocalDateTime.now());

        return repository.save(converted);
    }

    public List<ProdutoEntity> getAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public ProdutoEntity getByCodigoDeBarras(String codigo) {
        return repository.findByCodBarras(codigo);
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + UUID.randomUUID() + "_" + originalFilename;
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        return filePath;
    }

}
