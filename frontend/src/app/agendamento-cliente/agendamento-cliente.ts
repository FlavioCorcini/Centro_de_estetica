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

  areas: any[] = []; 

  constructor(private agendamentoService: AgendamentoService) {}

  ngOnInit(): void {
    this.carregarAreas();
  }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe({
      next: (dados) => {
        this.areas = dados; 
        console.log('Áreas carregadas:', dados); 
      },
      error: (erro) => {
        console.error('Erro ao buscar áreas:', erro);
      }
    });
  }

  selecionarArea(area: any) {
    console.log('Cliente clicou na área:', area.nome);
  }
}
