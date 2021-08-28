package com.cursoSpring.vendas.rest.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cursoSpring.vendas.domain.entity.ItemPedido;
import com.cursoSpring.vendas.domain.entity.Pedido;
import com.cursoSpring.vendas.domain.enums.StatusPedido;
import com.cursoSpring.vendas.rest.dto.AtualizacaoStatusDTO;
import com.cursoSpring.vendas.rest.dto.InformacaoItemPedidoDTO;
import com.cursoSpring.vendas.rest.dto.InformacaoPedidoDTO;
import com.cursoSpring.vendas.rest.dto.PedidoDTO;
import com.cursoSpring.vendas.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService service;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Integer save(@RequestBody @Valid PedidoDTO dto) {
		Pedido pedido = service.salvarPedido(dto);
		return pedido.getId();
	}
	
	@GetMapping("/{id}")
	public InformacaoPedidoDTO getById(@PathVariable Integer id) {
		return service.obterPedidoCompleto(id).map(p -> converter(p)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
	}
	
	@PatchMapping("/{id}")
	public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusDTO dto) {
		String novoStatus = dto.getNovoStatus();
		service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
	}
	
	private InformacaoPedidoDTO converter(Pedido pedido) {
		return InformacaoPedidoDTO.builder()
				.codigoo(pedido.getId())
				.dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.cpf(pedido.getCliente().getCpf())
				.nomeCliente(pedido.getCliente().getNome())
				.total(pedido.getTotal())
				.status(pedido.getStatusPedido().name())
				.itens(converter(pedido.getItens()))
				.build();		
	}

	private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
		if(CollectionUtils.isEmpty(itens)) {
			return Collections.emptyList();
		}
		return itens.stream().map(item -> InformacaoItemPedidoDTO.builder()
				.descricaoProduto(item.getProduto().getDescricao())
				.precoUnitario(item.getProduto().getPreco())
				.quantidade(item.getQuantidade())
				.build()).collect(Collectors.toList());
	}
}
