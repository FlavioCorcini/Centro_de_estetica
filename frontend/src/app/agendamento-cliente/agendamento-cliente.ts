import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-agendamento-cliente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './agendamento-cliente.html',
  styleUrls: ['./agendamento-cliente.css']
})
export class AgendamentoClienteComponent implements OnInit {

  passoAtual: number = 1;
  
  areas: any[] = [];
  servicos: any[] = [];
  
  areaSelecionada: any = null;
  servicoSelecionado: any = null;

  constructor(private agendamentoService: AgendamentoService) {}

  ngOnInit(): void {
    this.carregarAreas();
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (dados) => this.areas = dados,
      error: (erro) => console.error('Erro ao buscar áreas', erro)
    });
  }

  selecionarArea(area: any) {
    console.log('Área selecionada:', area);
    this.areaSelecionada = area;
    this.passoAtual = 2; 
    this.carregarServicos(area.id); 
  }

  carregarServicos(idArea: number) {
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (dados) => this.servicos = dados,
      error: (erro) => console.error('Erro ao buscar serviços', erro)
    });
  }

  selecionarServico(servico: any) {
    console.log('Serviço selecionado:', servico);
    this.servicoSelecionado = servico;
    alert(`Você escolheu: ${servico.nome}`); 
  }

  voltar() {
    if (this.passoAtual > 1) {
      this.passoAtual--;
    }
  }
}
