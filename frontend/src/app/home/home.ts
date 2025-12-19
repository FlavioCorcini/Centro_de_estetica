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
  
  // NOVAS VARIÁVEIS
  observacoesCliente: string = ''; 
  dataMinima: string = '';

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

  // Define a data mínima como HOJE para o calendário
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
        
        // Se a data selecionada for hoje, ignora horários que já passaram
        if (this.dataSelecionada === hojeFormatado) {
          const [horaSlot, minSlot] = horario.split(':').map(Number);
          const dataSlot = new Date();
          dataSlot.setHours(horaSlot, minSlot, 0);
          
          if (dataSlot > agora) {
            slots.push(horario);
          }
        } else {
          slots.push(horario);
        }
      }
    }
    this.horariosDisponiveis = slots;
  }

  atualizarDisponibilidade() {
    if (!this.dataSelecionada || !this.profissionalSelecionado) return;
    
    // Sempre que mudar a data, regera a grade para filtrar horários passados se for HOJE
    this.gerarGradeHorarios();

    const idFunc = this.profissionalSelecionado.id_usuario || this.profissionalSelecionado.id;
    if (!idFunc || idFunc === null) return; 

    this.agendamentoService.buscarAgendaDoDia(idFunc, this.dataSelecionada).subscribe({
      next: (d: any) => {
        this.agendamentosOcupados = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar agenda ocupada:", e)
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
      const minutosFim = minutosInicio + duracao;

      return minutosSlot >= minutosInicio && minutosSlot < minutosFim;
    });
  }

  selecionarHora(h: string) {
    this.horaSelecionada = h;
    this.cdr.detectChanges();
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (d) => {
        this.areas = d;
        this.cdr.detectChanges();
      }
    });
  }

  carregarServicos(idArea: number) {
    this.servicos = []; 
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (d) => {
        this.servicos = d;
        this.cdr.detectChanges();
      }
    });
  }

  carregarProfissionais() {
    this.profissionais = [];
    const idServico = this.servicoSelecionado?.id;
    if (idServico) {
      this.agendamentoService.listarFuncionarios(idServico).subscribe({
        next: (d) => {
          this.profissionais = d;
          this.cdr.detectChanges();
        }
      });
    }
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

  voltar() {
    if (this.passoAtual > 1) {
      this.passoAtual--;
      this.cdr.detectChanges();
    }
  }

  finalizarAgendamento() {
    const idFunc = this.profissionalSelecionado?.id_usuario || this.profissionalSelecionado?.id;

    const usuarioLogado = JSON.parse(localStorage.getItem('usuario') || '{}');
  const idClienteLogado = usuarioLogado.id;

  if (!idClienteLogado) {
    alert("Erro: Usuário não identificado. Por favor, faça login novamente.");
    this.router.navigate(['/login']);
    return;
  }
    const agendamento = {
      idCliente: idClienteLogado, 
      idFuncionario: idFunc, 
      idServico: this.servicoSelecionado.id,
      dataHora: `${this.dataSelecionada}T${this.horaSelecionada}:00`, 
      observacoes: this.observacoesCliente || "Sem observações"
    };

    this.agendamentoService.finalizarAgendamento(agendamento).subscribe({
      next: (res) => {
        this.passoAtual = 5;
        this.cdr.detectChanges();
      },
      error: (err) => alert("Erro ao salvar. Verifique se o horário ainda está disponível.")
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
    this.cdr.detectChanges();
    
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}