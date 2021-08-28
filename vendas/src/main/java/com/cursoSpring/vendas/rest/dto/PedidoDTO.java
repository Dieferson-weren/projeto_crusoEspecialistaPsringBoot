package com.cursoSpring.vendas.rest.dto;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.cursoSpring.vendas.validation.NotEmptyList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
	
	@NotNull(message = "{campo.codigo-cliente.obrigatorio}")
	private Integer cliente;
	
	@NotEmptyList(message = "{}campo.itens-pedido.obrigatório")
	private List<ItemPedidoDTO> itens;
}
