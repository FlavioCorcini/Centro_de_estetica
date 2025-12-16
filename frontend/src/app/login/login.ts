import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth';
import { Router } from '@angular/router';
import { NgxMaskDirective } from 'ngx-mask'; 
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  cadastroForm: FormGroup;
  
  // Variável que decide qual tela mostrar (começa no login)
  isLoginMode = true; 
  mostrarSenha = false;
  erroLogin = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // 1. Formulário de Login
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });

    // 2. Formulário de Cadastro (Nome, Email, WhatsApp, Senha)
    this.cadastroForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', Validators.required, Validators.email],      
      telefone: ['', Validators.required], // Campo WhatsApp
      senha: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  // Função para trocar de tela
  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.erroLogin = false; // Limpa erros antigos
  }

  toggleSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

  

  onSubmit() {
    // LÓGICA DE LOGIN
    if (this.isLoginMode) {
      if (this.loginForm.valid) {
        this.authService.login(this.loginForm.value).subscribe({
          next: (usuario) => {
            console.log('Login Sucesso:', usuario);
            localStorage.setItem('usuario', JSON.stringify(usuario));
            alert("Bem-vindo(a) " + usuario.nome);
            this.router.navigate(['/home']); // Descomente quando criar a home
          },
          error: (err) => {
            this.erroLogin = true;
          }
        });
      }
    
    // LÓGICA DE CADASTRO
    } else {
      if (this.cadastroForm.valid) {

        // Um objeto novo misturando os dados do form + o perfil fixo
        const dadosCadastro = {
            ...this.cadastroForm.value,  // Pega (nome, email, senha, telefone)
            tipo: 'CLIENTE',            
            statusUsuario: 'ATIVO'
        };

        this.authService.cadastrar(dadosCadastro).subscribe({
          next: () => {
            alert('Cadastro realizado com sucesso! Faça login.');
            this.toggleMode(); // Volta para a tela de login automaticamente
          },
          error: (err) => {
            alert('Erro ao cadastrar. Tente novamente.');
            console.error(err);
          }
        });
      }
    }
  }

}