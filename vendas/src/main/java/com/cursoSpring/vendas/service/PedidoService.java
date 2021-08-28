package com.cursoSpring.vendas.service;

import java.util.Optional;

import com.cursoSpring.vendas.domain.entity.Pedido;
import com.cursoSpring.vendas.domain.enums.StatusPedido;
import com.cursoSpring.vendas.rest.dto.PedidoDTO;

public interface PedidoService {
	Pedido salvarPedido(PedidoDTO dto);
	Optional<Pedido> obterPedidoCompleto(Integer id);
	void atualizaStatus(Integer id, StatusPedido statusPedido);
}
