package com.cursoSpring.vendas.exception;

public class PedidoNaoEncoontradoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public PedidoNaoEncoontradoException() {
		super("Pedido não encontrado");
	}

}
