import { Injectable } from '@angular/core'; // Supondo que seja Angular pelo .ts
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AgendamentoService {
  private apiUrl = 'http://localhost:8080'; // Endereço do seu Java

  constructor(private http: HttpClient) {}
  
  listarAreas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/areas`);
  }

  listarServicosPorArea(idArea: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/servicos/area/${idArea}`);
  }

  listarHorarios(idFuncionario: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/horarios/funcionario/${idFuncionario}`);
  }
}
