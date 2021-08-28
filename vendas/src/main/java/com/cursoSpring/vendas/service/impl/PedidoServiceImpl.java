package com.cursoSpring.vendas.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursoSpring.vendas.domain.entity.Cliente;
import com.cursoSpring.vendas.domain.entity.ItemPedido;
import com.cursoSpring.vendas.domain.entity.Pedido;
import com.cursoSpring.vendas.domain.entity.Produto;
import com.cursoSpring.vendas.domain.enums.StatusPedido;
import com.cursoSpring.vendas.domain.repository.ClienteRepository;
import com.cursoSpring.vendas.domain.repository.ItemPedidoRepository;
import com.cursoSpring.vendas.domain.repository.PedidosRepository;
import com.cursoSpring.vendas.domain.repository.ProdutoRepository;
import com.cursoSpring.vendas.exception.PedidoNaoEncoontradoException;
import com.cursoSpring.vendas.exception.RegraNegocioException;
import com.cursoSpring.vendas.rest.dto.ItemPedidoDTO;
import com.cursoSpring.vendas.rest.dto.PedidoDTO;
import com.cursoSpring.vendas.service.PedidoService;



@Service
public class PedidoServiceImpl implements PedidoService{
	
	@Autowired
	private  PedidosRepository pedidoRepository;
	
	@Autowired
	private  ClienteRepository clienteRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoReposytori;
	
	@Override
	@Transactional
	public Pedido salvarPedido(PedidoDTO dto) {
		Integer idCliente = dto.getCliente();
		Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Cliente não encontrado"));
		Pedido pedido = new Pedido();
		
		pedido.setDataPedido(LocalDate.now());
		pedido.setCliente(cliente);
		pedido.setStatusPedido(StatusPedido.REALIZADO);
		List<ItemPedido> itens = converter(pedido, dto.getItens());
		pedidoRepository.save(pedido);
		itemPedidoReposytori.saveAll(itens);
		pedido.setItens(itens);
		pedido.setTotal(pedido.getTotal());
		return pedido;
	}
	
	

	private List<ItemPedido> converter(Pedido pedido, List<ItemPedidoDTO> itens) {
		if(itens.isEmpty()) {
			throw new RegraNegocioException("A lista de itens não pode estar vazia");
		}
		return itens.stream().map(dto -> {
			Integer idProduto = dto.getProduto();
			Produto produto = produtoRepository.findById(idProduto)
					.orElseThrow(() -> new RegraNegocioException("Código de produto inválido" + idProduto));
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setQuantidade(dto.getQuantidade());
			itemPedido.setPedido(pedido);
			itemPedido.setProduto(produto);
			return itemPedido;
		}).collect(Collectors.toList());
	}



	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		return pedidoRepository.findByIdFetchItens(id);
	}



	@Override
	@Transactional
	public void atualizaStatus(Integer id, StatusPedido statusPedido) {
		pedidoRepository.findById(id).map(pedido -> {
			pedido.setStatusPedido(statusPedido);
			return pedidoRepository.save(pedido);
		}).orElseThrow(() -> new PedidoNaoEncoontradoException());
		
	}

}
