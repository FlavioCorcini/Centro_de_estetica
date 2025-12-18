import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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
  
  // Grade de horários
  horariosDisponiveis: string[] = [];
  agendamentosOcupados: any[] = [];

  areaSelecionada: any = null;
  servicoSelecionado: any = null;
  profissionalSelecionado: any = null;
  dataSelecionada: string = '';
  horaSelecionada: string = '';

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    this.carregarAreas();
    this.gerarGradeHorarios();
  }

  // Gera slots de 15 em 15 minutos (08:00 até 18:45)
  gerarGradeHorarios() {
    const slots = [];
    for (let h = 8; h <= 18; h++) {
      for (let m = 0; m < 60; m += 15) {
        slots.push(`${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`);
      }
    }
    this.horariosDisponiveis = slots;
  }

  // Busca no Java os agendamentos daquela data para o profissional
  atualizarDisponibilidade() {
    if (!this.dataSelecionada || !this.profissionalSelecionado) return;

    const idFunc = this.profissionalSelecionado.id_usuario || this.profissionalSelecionado.id;
    
    if (!idFunc) return; 

    this.agendamentoService.buscarAgendaDoDia(idFunc, this.dataSelecionada).subscribe({
      next: (d: any) => {
        this.agendamentosOcupados = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar agenda ocupada:", e)
    });
  }

  /**
   * LÓGICA ATUALIZADA: Bloqueia o horário de início e toda a duração do serviço.
   * Ex: Se um serviço de 30min começa às 08:00, os slots 08:00 e 08:15 ficam ocupados.
   */
  estaOcupado(horarioSlot: string): boolean {
    if (!this.agendamentosOcupados || this.agendamentosOcupados.length === 0) return false;

    // Converte o horário do botão (ex: "08:15") para minutos totais desde o início do dia
    const [hSlot, mSlot] = horarioSlot.split(':').map(Number);
    const minutosSlot = hSlot * 60 + mSlot;

    return this.agendamentosOcupados.some(ag => {
      // 1. Pega o início do agendamento (ex: "08:00")
      const horaInicioAg = ag.dataHora.split('T')[1].substring(0, 5);
      const [hInicio, mInicio] = horaInicioAg.split(':').map(Number);
      const minutosInicio = hInicio * 60 + mInicio;

      // 2. Pega a duração (tenta buscar tempoAtendimento ou tempo_atendimento)
      // Se não vier nada do banco, assume 30 minutos por padrão
      const duracao = parseInt(ag.servico?.tempoAtendimento || ag.servico?.tempo_atendimento || 30);
      const minutosFim = minutosInicio + duracao;

      // 3. Verifica se o slot atual está "dentro" do intervalo do agendamento
      return minutosSlot >= minutosInicio && minutosSlot < minutosFim;
    });
  }

  selecionarHora(h: string) {
    this.horaSelecionada = h;
    this.cdr.detectChanges();
  }

  // --- MÉTODOS DE NAVEGAÇÃO E CARREGAMENTO ---

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (d) => {
        this.areas = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar áreas:", e)
    });
  }

  carregarServicos(idArea: number) {
    this.servicos = []; 
    this.agendamentoService.listarServicosPorArea(idArea).subscribe({
      next: (d) => {
        this.servicos = d;
        this.cdr.detectChanges();
      },
      error: (e) => console.error("Erro ao carregar serviços:", e)
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
        },
        error: (e) => console.error("Erro ao carregar profissionais:", e)
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

    const agendamento = {
      idCliente: 5, 
      idFuncionario: idFunc, 
      idServico: this.servicoSelecionado.id,
      dataHora: `${this.dataSelecionada}T${this.horaSelecionada}:00`, 
      observacoes: "Agendado via Web"
    };

    this.agendamentoService.finalizarAgendamento(agendamento).subscribe({
      next: (res) => {
        // Redireciona para o Passo 5 (Sucesso)
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
    this.cdr.detectChanges();
  }
}