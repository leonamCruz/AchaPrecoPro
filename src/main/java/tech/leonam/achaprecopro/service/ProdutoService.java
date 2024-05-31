package tech.leonam.achaprecopro.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tech.leonam.achaprecopro.model.ProdutoDTO;
import tech.leonam.achaprecopro.model.ProdutoEntity;
import tech.leonam.achaprecopro.repository.ProdutoRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {

    private ProdutoRepository repository;
    private ModelMapper modelMapper;

    public ProdutoEntity save(ProdutoDTO dto){
        var converted = modelMapper.map(dto, ProdutoEntity.class);
        return repository.save(converted);
    }

    public ProdutoEntity save(ProdutoDTO dto, Long id){
        var converted = modelMapper.map(dto, ProdutoEntity.class);
        converted.setId(id);
        return repository.save(converted);
    }

    public List<ProdutoEntity> getAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
