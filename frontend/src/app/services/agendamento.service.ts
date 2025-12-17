import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

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
    return this.http.get<any[]>(`${this.apiUrl}/funcionarios/servico/${idServico}`);
  }

  finalizarAgendamento(agendamento: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/agendamentos`, agendamento);
  }
}