package com.cursoSpring.vendas.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cursoSpring.vendas.exception.PedidoNaoEncoontradoException;
import com.cursoSpring.vendas.exception.RegraNegocioException;
import com.cursoSpring.vendas.rest.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(RegraNegocioException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiErrors handleRegraNegocio(RegraNegocioException ex) {
		return new ApiErrors(ex.getMessage());
	}
	
	@ExceptionHandler(PedidoNaoEncoontradoException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ApiErrors handlePedidoNaoEncontrado(PedidoNaoEncoontradoException ex) {
		return new ApiErrors(ex.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
		List<String> erros =  ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
		return new ApiErrors(erros);
	}

}
