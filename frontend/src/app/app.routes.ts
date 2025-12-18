import { Routes } from '@angular/router';
import { LoginComponent } from './login/login'; 
import { HomeComponent } from './home/home'; 
import { MeusAgendamentosComponent } from './meus-agendamentos/meus-agendamentos'; 

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, 
    { path: 'login', component: LoginComponent },       
    { path: 'home', component: HomeComponent },         
    { path: 'meus-agendamentos', component: MeusAgendamentosComponent }
];
