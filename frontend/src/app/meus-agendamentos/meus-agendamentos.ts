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
  idCliente: number = 5; // ID fixo para teste, conforme seu home.ts

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarHistorico();
  }

  carregarHistorico() {
    this.carregando = true;
    // Assume que você tem esse método no agendamento.service.ts
    this.agendamentoService.listarPorCliente(this.idCliente).subscribe({
      next: (dados) => {
        // Ordena por data (mais recentes primeiro)
        this.listaAgendamentos = dados.sort((a: any, b: any) => 
          new Date(b.dataHora).getTime() - new Date(a.dataHora).getTime()
        );
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erro ao carregar histórico:", err);
        this.carregando = false;
      }
    });
  }

  cancelar(idAgendamento: number) {
    if (confirm("Tem certeza que deseja cancelar este agendamento?")) {
      this.agendamentoService.cancelarAgendamento(idAgendamento).subscribe({
        next: () => {
          alert("Agendamento cancelado com sucesso!");
          this.carregarHistorico(); // Recarrega a lista
        },
        error: (err) => alert("Erro ao cancelar agendamento.")
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONCLUIDO': return 'status-verde';
      case 'CANCELADO': return 'status-vermelho';
      default: return 'status-amarelo';
    }
  }
}