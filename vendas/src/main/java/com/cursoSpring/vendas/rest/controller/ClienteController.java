package com.cursoSpring.vendas.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cursoSpring.vendas.domain.entity.Cliente;
import com.cursoSpring.vendas.domain.repository.ClienteRepository;

@RestController
@RequestMapping("clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository repository;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cliente salvar(@RequestBody @Valid Cliente cliente) {
		return repository.save(cliente);
	}
	
	@GetMapping("/{id}")
	public Cliente findById(@PathVariable Integer id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		repository.findById(id).map(x -> {
			repository.delete(x);
			return void.class;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
		repository.findById(id).map(x -> {
			cliente.setId(x.getId());
			repository.save(cliente);
			return x;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado")); 
	}
	
	@GetMapping
	public List<Cliente> find(Cliente filtro){
		ExampleMatcher match = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		
		Example<Cliente> example = Example.of(filtro, match);
		
		return repository.findAll(example);
	}
}
