import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {

  passoAtual: number = 1;
  
  areas: any[] = [];
  servicos: any[] = [];
  profissionais: any[] = [];

  areaSelecionada: any = null;
  servicoSelecionado: any = null;
  profissionalSelecionado: any = null;
  dataSelecionada: string = '';
  horaSelecionada: string = '';

  constructor(private agendamentoService: AgendamentoService) {}

  ngOnInit(): void {
    this.carregarAreas();
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (d) => this.areas = d,
      error: (e) => console.error(e)
    });
  }

  carregarServicos(idArea: number) {
    this.servicos = []; 
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (d) => this.servicos = d,
      error: (e) => console.error(e)
    });
  }

  carregarProfissionais() {
    this.profissionais = [];
        const idServicoSelecionado = this.servicoSelecionado?.id;
    if (idServicoSelecionado) {
        this.agendamentoService.listarFuncionarios(idServicoSelecionado).subscribe({
            next: (d) => this.profissionais = d, 
            error: (e) => console.error(e)
        });
    }
}

selecionarArea(area: any) { 
    this.areaSelecionada = area; 
    if (this.areaSelecionada) {
        this.proximoPasso(); 
    }
}  
  selecionarServico(servico: any) { 
    this.servicoSelecionado = servico; 
    this.passoAtual = 3; 
    this.carregarProfissionais();
  }

  selecionarProfissional(prof: any) {
    this.profissionalSelecionado = prof;
    this.passoAtual = 4; 
  }

  proximoPasso() {
    if (this.passoAtual === 1 && this.areaSelecionada) {
      this.passoAtual = 2;
      this.carregarServicos(this.areaSelecionada.id); 
    } else if (this.passoAtual === 4 && this.dataSelecionada && this.horaSelecionada) {
      this.finalizarAgendamento();
    }
}

  voltar() {
    if (this.passoAtual > 1) {
      this.passoAtual--;
      if(this.passoAtual === 1) this.servicoSelecionado = null;
      if(this.passoAtual === 2) this.profissionalSelecionado = null;
    }
  }

  finalizarAgendamento() {
    const agendamento = {
      idCliente: 1, 
      idFuncionario: this.profissionalSelecionado.id || null, 
      idsServicos: [this.servicoSelecionado.id],
      dataHora: `${this.dataSelecionada}T${this.horaSelecionada}:00`, 
      observacoes: "Agendado pelo App"
    };

    console.log("Enviando para o Back-end:", agendamento);
    alert("Agendamento Enviado! (Olhe o console)");
  }
}