package com.cursoSpring.vendas.rest.controller;

import javax.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cursoSpring.vendas.domain.entity.Usuario;
import com.cursoSpring.vendas.exception.SenhaInvalidaException;
import com.cursoSpring.vendas.rest.dto.CredenciaisDTO;
import com.cursoSpring.vendas.rest.dto.TokenDTO;
import com.cursoSpring.vendas.security.jwt.JwtService;
import com.cursoSpring.vendas.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UserDetailsServiceImpl userservice;
	private final PasswordEncoder encoder;
	private final JwtService jwtService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Usuario salvar(@RequestBody @Valid Usuario usuario) {
		String senhaCriptografa = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografa);
		return userservice.salvar(usuario);
	}
	
	@PostMapping("/auth")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Usuario usuario = Usuario.builder()
					.login(credenciais.getLogin())
					.senha(credenciais.getSenha()).build();
			UserDetails usuarioAutenticado = userservice.autenticar(usuario);
			String token = jwtService.gerarToken(usuario);
			return new TokenDTO(usuario.getLogin(), token);
		} catch (UsernameNotFoundException  | SenhaInvalidaException e) {
			throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} 
	}
}
