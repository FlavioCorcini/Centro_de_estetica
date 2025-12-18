import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AgendamentoService {

  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  listarAreas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/areas`); 
  }

  listarServicosPorArea(idArea: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/servicos/por-area/${idArea}`); 
  }

  listarFuncionarios(idServico: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuarios/servico/${idServico}`);
  }

  /**
   * NOVO MÉTODO: Busca a agenda ocupada do profissional para uma data específica.
   * Isso permite ao Front-end saber quais slots de 15 min devem ser bloqueados.
   */
  buscarAgendaDoDia(usuarioId: number, data: string): Observable<any[]> {
    // Exemplo de chamada: http://localhost:8080/agendamentos/agenda-diaria?usuarioId=2&data=2025-12-30
    return this.http.get<any[]>(`${this.apiUrl}/agendamentos/agenda-diaria`, {
      params: { usuarioId, data }
    });
  }

  finalizarAgendamento(agendamento: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/agendamentos`, agendamento);
  }
}