package tech.leonam.achaprecopro.service;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.leonam.achaprecopro.model.ProdutoEntity;
import tech.leonam.achaprecopro.model.ProdutoSaveDTO;
import tech.leonam.achaprecopro.repository.ProdutoRepository;

import java.io.FileNotFoundException;
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
    @Value("${diretorio}")
    private String UPLOAD_DIR;

    @Autowired
    public ProdutoService(ProdutoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public ProdutoEntity save(ProdutoSaveDTO dto, MultipartFile imagem) throws IOException {
        if(repository.existsProdutoEntityByCodBarras(dto.getCodBarras())){
            throw new EntityExistsException("Código de BArras já cadastrado");
        }

        var converted = modelMapper.map(dto, ProdutoEntity.class);
        converted.setUltimaAlteracao(LocalDateTime.now());

        String imagePath = saveImage(imagem);
        converted.setLocalizacaoDaImagem(imagePath);

        return repository.save(converted);
    }

    public ProdutoEntity alteraComImagem(ProdutoSaveDTO dto, Long id, MultipartFile imagem) throws IOException {
        var entidadeDoBancoDeDados = repository.findById(id).orElseThrow();
        deletaImagem(entidadeDoBancoDeDados.getLocalizacaoDaImagem());

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

    private void deletaImagem(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Imagem não localizada: " + path);
        }

        Files.delete(filePath);
    }

}