import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { AgendamentoService } from '../services/agendamento.service';
import { Router } from '@angular/router';

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
  
  horariosDisponiveis: string[] = [];
  agendamentosOcupados: any[] = [];

  areaSelecionada: any = null;
  servicoSelecionado: any = null;
  profissionalSelecionado: any = null;
  dataSelecionada: string = '';
  horaSelecionada: string = '';
  
  observacoesCliente: string = ''; 
  dataMinima: string = '';

  agendamentoRealizado: any = null;

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarAreas();
    this.definirDataMinima();
    this.gerarGradeHorarios();
  }

  definirDataMinima() {
    const hoje = new Date();
    const dia = String(hoje.getDate()).padStart(2, '0');
    const mes = String(hoje.getMonth() + 1).padStart(2, '0');
    const ano = hoje.getFullYear();
    this.dataMinima = `${ano}-${mes}-${dia}`;
  }

  gerarGradeHorarios() {
    const slots = [];
    const agora = new Date();
    const hojeFormatado = this.dataMinima;

    for (let h = 8; h <= 18; h++) {
      for (let m = 0; m < 60; m += 15) {
        const horario = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
        
        if (this.dataSelecionada === hojeFormatado) {
          const [horaSlot, minSlot] = horario.split(':').map(Number);
          const dataSlot = new Date();
          dataSlot.setHours(horaSlot, minSlot, 0);
          if (dataSlot > agora) slots.push(horario);
        } else {
          slots.push(horario);
        }
      }
    }
    this.horariosDisponiveis = slots;
  }

  // PEGA AS ÁREAS DO JAVA
  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (d) => {
        console.log("Áreas vindas do Banco:", d);
        this.areas = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro: O Java não retornou as Áreas. Verifique o Banco!", e)
    });
  }

  // PEGA OS SERVIÇOS DO JAVA
  carregarServicos(idArea: number) {
    this.servicos = []; 
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (d) => {
        console.log("Serviços vindos do Banco:", d);
        this.servicos = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao buscar serviços no banco", e)
    });
  }

  // PEGA OS FUNCIONÁRIOS DO JAVA
  carregarProfissionais() {
    this.profissionais = [];
    const idServico = this.servicoSelecionado?.id;
    if (idServico) {
      this.agendamentoService.listarFuncionarios(idServico).subscribe({
        next: (d) => {
          console.log("Profissionais vindos do Banco:", d);
          this.profissionais = d;
          this.cdr.detectChanges();
        },
        error: (e) => console.error("Erro ao buscar funcionários no banco", e)
      });
    }
  }

  atualizarDisponibilidade() {
    if (!this.dataSelecionada || !this.profissionalSelecionado) return;
    this.gerarGradeHorarios();
    const idFunc = this.profissionalSelecionado.id_usuario || this.profissionalSelecionado.id;
    if (!idFunc) return; 

    this.agendamentoService.buscarAgendaDoDia(idFunc, this.dataSelecionada).subscribe({
      next: (d: any) => {
        this.agendamentosOcupados = d;
        this.cdr.detectChanges();
      }
    });
  }

  estaOcupado(horarioSlot: string): boolean {
    if (!this.agendamentosOcupados || this.agendamentosOcupados.length === 0) return false;
    const [hSlot, mSlot] = horarioSlot.split(':').map(Number);
    const minutosSlot = hSlot * 60 + mSlot;
    return this.agendamentosOcupados.some(ag => {
      const horaInicioAg = ag.dataHora.split('T')[1].substring(0, 5);
      const [hInicio, mInicio] = horaInicioAg.split(':').map(Number);
      const minutosInicio = hInicio * 60 + mInicio;
      const duracao = parseInt(ag.servico?.tempoAtendimento || ag.servico?.tempo_atendimento || 30);
      return minutosSlot >= minutosInicio && minutosSlot < (minutosInicio + duracao);
    });
  }

  selecionarArea(area: any) { 
    this.areaSelecionada = area; 
    this.carregarServicos(area.id); 
    this.passoAtual = 2; 
  }

  selecionarServico(servico: any) { 
    this.servicoSelecionado = servico; 
    this.carregarProfissionais();
    this.passoAtual = 3; 
  }

  selecionarProfissional(prof: any) {
    this.profissionalSelecionado = prof;
    this.passoAtual = 4; 
    this.atualizarDisponibilidade(); 
  }

  selecionarHora(h: string) {
    this.horaSelecionada = h;
    this.cdr.detectChanges();
  }

  voltar() {
    if (this.passoAtual > 1) {
      this.passoAtual--;
      this.cdr.detectChanges();
    }
  }

  finalizarAgendamento() {
    const idFunc = this.profissionalSelecionado?.id_usuario || this.profissionalSelecionado?.id;
    const usuarioLogado = JSON.parse(localStorage.getItem('usuario') || '{}');
    const idClienteLogado = usuarioLogado.id || 5; 

    const agendamentoDTO = {
      idCliente: idClienteLogado, 
      idFuncionario: idFunc, 
      idServico: this.servicoSelecionado.id,
      dataHora: `${this.dataSelecionada}T${this.horaSelecionada}:00`, 
      observacoes: this.observacoesCliente || "Sem observações"
    };

    this.agendamentoService.finalizarAgendamento(agendamentoDTO).subscribe({
      next: (res: any) => {
        this.agendamentoRealizado = { ...res, status: 'AGENDADO' };
        this.passoAtual = 5;
        this.cdr.detectChanges();
      },
      error: (err) => alert("Erro ao salvar no banco. O Java está ligado?")
    });
  }

  resetarFluxo() {
    this.passoAtual = 1;
    this.areaSelecionada = null;
    this.servicoSelecionado = null;
    this.profissionalSelecionado = null;
    this.dataSelecionada = '';
    this.horaSelecionada = '';
    this.observacoesCliente = '';
    this.agendamentosOcupados = [];
    this.agendamentoRealizado = null;
    this.cdr.detectChanges();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}