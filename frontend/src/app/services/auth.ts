import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// Agora este import vai funcionar porque criamos o arquivo acima:
import { LoginRequestDTO } from '../dto/login-request.dto'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  login(credenciais: LoginRequestDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, credenciais);
  }
  cadastrar(dados: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/usuarios`, dados);
  }
}