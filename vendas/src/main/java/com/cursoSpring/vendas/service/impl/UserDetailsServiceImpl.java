package com.cursoSpring.vendas.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cursoSpring.vendas.domain.entity.Usuario;
import com.cursoSpring.vendas.domain.repository.UsuarioRepository;
import com.cursoSpring.vendas.exception.SenhaInvalidaException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioRepository repository;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getLogin());
		boolean senhaValida = passwordEncoder.matches(usuario.getSenha(), user.getPassword());
		if(senhaValida) {
			return user;
		}
		throw new SenhaInvalidaException();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = repository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "não encontrado"));
		
		String[] roles = usuario.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[] {"USER"};
		
		return User.builder()
				.username(usuario.getLogin())
				.password(usuario.getSenha())
				.roles(roles)
				.build();
	}

}
