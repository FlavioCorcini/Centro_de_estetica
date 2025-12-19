import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-meus-agendamentos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './meus-agendamentos.html',
  styleUrls: ['./meus-agendamentos.css']
})
export class MeusAgendamentosComponent implements OnInit {

  listaAgendamentos: any[] = [];
  carregando: boolean = true;
  
  // Mudamos para iniciar vazio e pegar do login ou padrão
  idCliente: number = 5; 

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // BUSCA O ID DO USUÁRIO LOGADO (Igual você fez no home.ts)
    const usuarioLogado = JSON.parse(localStorage.getItem('usuario') || '{}');
    
    if (usuarioLogado && usuarioLogado.id) {
      this.idCliente = usuarioLogado.id;
    } else {
      // Caso não ache usuário logado, mantemos o 5 para teste
      this.idCliente = 5;
    }

    this.carregarHistorico();
  }

  carregarHistorico() {
    this.carregando = true;
    
    // Agora ele busca usando o ID correto que foi definido no ngOnInit
    this.agendamentoService.listarPorCliente(this.idCliente).subscribe({
      next: (dados) => {
        // Log para você ver no console (F12) se os dados estão chegando
        console.log("Agendamentos recebidos do banco:", dados);

        const dadosTratados = dados.map((ag: any) => {
          // Se o Java mandar como PENDENTE, forçamos AGENDADO para aparecer com a cor certa
          if (ag.status?.toUpperCase() === 'PENDENTE') {
            return { ...ag, status: 'AGENDADO' };
          }
          return ag;
        });

        // Ordena por data (mais recentes primeiro)
        this.listaAgendamentos = dadosTratados.sort((a: any, b: any) => 
          new Date(b.dataHora).getTime() - new Date(a.dataHora).getTime()
        );

        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erro ao carregar histórico:", err);
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cancelar(idAgendamento: number) {
    if (!idAgendamento) return;

    if (confirm("Tem certeza que deseja cancelar este agendamento?")) {
      this.agendamentoService.cancelarAgendamento(idAgendamento).subscribe({
        next: () => {
          alert("Agendamento cancelado com sucesso!");
          this.carregarHistorico(); 
        },
        error: (err) => {
          console.error("Erro ao cancelar:", err);
          alert("Erro ao cancelar agendamento no servidor.");
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONCLUIDO': return 'status-verde';
      case 'AGENDADO': return 'status-verde';
      case 'CANCELADO': return 'status-vermelho';
      default: return 'status-amarelo';
    }
  }
}