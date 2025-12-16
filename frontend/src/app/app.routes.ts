import { Routes } from '@angular/router';
import { LoginComponent } from './login/login'; // Importe seu componente
import { AgendamentoClienteComponent } from './agendamento-cliente/agendamento-cliente'; 

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redireciona a raiz para o login
    { path: 'login', component: LoginComponent }          // Define a rota do login
    { path: 'agendamento-cliente', component: AgendamentoClienteComponent }
];
