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


  buscarAgendaDoDia(usuarioId: number, data: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/agendamentos/agenda-diaria`, {
      params: { usuarioId, data }
    });
  }

  finalizarAgendamento(agendamento: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/agendamentos`, agendamento);
  }


  listarPorCliente(idCliente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/agendamentos/cliente/${idCliente}`);
  }

  cancelarAgendamento(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/agendamentos/${id}`);
  }
}