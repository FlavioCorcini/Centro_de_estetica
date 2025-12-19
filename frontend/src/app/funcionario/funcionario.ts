import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AgendamentoService } from '../services/agendamento.service';

@Component({
  selector: 'app-funcionario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './funcionario.html',
  styleUrls: ['./funcionario.css']
})
export class FuncionarioComponent implements OnInit {

  usuarioLogado: any = null;
  tipo: string = '';
  abaAtual: string = 'AGENDAMENTOS';

  listaClientes: any[] = [];
  listaFuncionarios: any[] = [];

  modalAberto: boolean = false;
  perfilCadastro: string = 'CLIENTE'; 

  usuarioEdicao: any = {
    id: null, nome: '', email: '', telefone: '', senha: '', cargo: '', ativo: true
  };

  agendaProfissional: any[] = [];
  dataVisualizacaoAgenda: string = '';

  diaExpandido: string | null = null;
  diasSemana: string[] = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'];
  slotsHorariosConfig: string[] = [];
  horariosConfigurados: { [key: string]: string[] } = {};

  areas: any[] = [];

  constructor(
    private agendamentoService: AgendamentoService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.verificarPermissao();
    this.inicializarDatas();
    this.gerarSlotsConfiguracao();

    if (this.usuarioLogado) {
      this.carregarHorariosSalvos();
      this.carregarAgendaDoProfissional();
      this.carregarUsuariosDoSistema(); 
    }
  }

  verificarPermissao() {
    const userJson = localStorage.getItem('usuario');
    if (userJson) {
      this.usuarioLogado = JSON.parse(userJson);
      this.tipo = this.usuarioLogado.tipo;

      if (this.tipo === 'CLIENTE') {
        this.router.navigate(['/home']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  mudarAba(novaAba: string) {
    this.abaAtual = novaAba;

    if (novaAba === 'AGENDAMENTOS') this.carregarAgendaDoProfissional();
    if (novaAba === 'AREAS') this.carregarAreas();
    if (novaAba === 'USUARIOS' || novaAba === 'FUNCIONARIOS') {
      this.carregarUsuariosDoSistema();
    }
  }

  // CORREÇÃO: Agora busca do AgendamentoService que é o seu único service de banco
  carregarUsuariosDoSistema() {
    this.agendamentoService.listarTodosUsuarios().subscribe({
      next: (usuarios) => {
        // Separa os dados vindos do banco real por tipo
        this.listaClientes = usuarios.filter(u => u.tipo === 'CLIENTE');
        this.listaFuncionarios = usuarios.filter(u => u.tipo !== 'CLIENTE');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erro ao carregar usuários do Java:", err);
        // Fallback para não quebrar a tela se o Java estiver off
        this.listaClientes = [];
        this.listaFuncionarios = [];
      }
    });
  }

  abrirModalCadastro(tipo: string) {
    this.perfilCadastro = tipo;
    this.usuarioEdicao = {
      id: null, nome: '', email: '', telefone: '', senha: '', cargo: '', ativo: true
    };
    this.modalAberto = true;
  }

  editarUsuario(usuario: any) {
    this.perfilCadastro = usuario.tipo || (this.abaAtual === 'FUNCIONARIOS' ? 'FUNCIONARIO' : 'CLIENTE');
    this.usuarioEdicao = { ...usuario, senha: '' }; 
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
  }

  // CORREÇÃO: Removido Math.random e adicionado chamada real ao service
  salvarUsuario() {
    if (!this.usuarioEdicao.nome || !this.usuarioEdicao.email) {
      alert('Por favor, preencha nome e email.');
      return;
    }

    this.usuarioEdicao.tipo = this.perfilCadastro;

    // Se tiver ID, atualiza. Se não tiver, cadastra novo no banco.
    const acao = this.usuarioEdicao.id 
      ? this.agendamentoService.atualizarUsuario(this.usuarioEdicao.id, this.usuarioEdicao)
      : this.agendamentoService.cadastrarUsuario(this.usuarioEdicao);

    acao.subscribe({
      next: () => {
        alert('Dados salvos no banco de dados com sucesso!');
        this.fecharModal();
        this.carregarUsuariosDoSistema(); // Recarrega a lista vinda do banco
      },
      error: (err) => {
        console.error(err);
        alert('Erro ao salvar no banco. Verifique se o Java está rodando.');
      }
    });
  }

  alternarStatus(usuario: any) {
    if (!usuario.id) return;
    const acao = usuario.ativo ? 'desativar' : 'ativar';
    
    if (confirm(`Tem certeza que deseja ${acao} o usuário ${usuario.nome}?`)) {
      this.agendamentoService.alterarStatusUsuario(usuario.id).subscribe({
        next: () => {
          this.carregarUsuariosDoSistema(); // Atualiza a lista após mudar o status no banco
        },
        error: (err) => alert("Erro ao mudar status no banco.")
      });
    }
  }

  // --- MANTIDOS MÉTODOS DE HORÁRIOS E AGENDA ---
  
  carregarHorariosSalvos() {
    if (!this.usuarioLogado?.id) return;
    this.agendamentoService.buscarHorariosTrabalho(this.usuarioLogado.id).subscribe({
      next: (intervalos: any[]) => {
        this.horariosConfigurados = {};
        intervalos.forEach(item => {
          const diaNome = this.converterEnumParaDiaNome(item.diaSemana);
          if (!this.horariosConfigurados[diaNome]) this.horariosConfigurados[diaNome] = [];
          this.preencherSlotsNoIntervalo(diaNome, item.horarioInicio, item.horarioFim);
        });
        this.cdr.detectChanges();
      }
    });
  }

  salvarHorarios() {
    if (!this.usuarioLogado?.id) return;
    let totalSalvo = 0;
    for (const diaNome of this.diasSemana) {
      const slots = this.horariosConfigurados[diaNome];
      if (slots && slots.length > 0) {
        slots.sort();
        const intervalos = this.converterSlotsEmIntervalos(slots);
        intervalos.forEach(intervalo => {
          const dto = {
            idFuncionario: this.usuarioLogado.id,
            diaSemana: this.converterDiaNomeParaEnum(diaNome),
            horarioInicio: intervalo.inicio,
            horarioFim: intervalo.fim
          };
          this.agendamentoService.salvarHorarioTrabalho(dto).subscribe();
          totalSalvo++;
        });
      }
    }
    alert(totalSalvo > 0 ? 'Horários processados!' : 'Nenhum horário selecionado.');
  }

  private converterSlotsEmIntervalos(slots: string[]): any[] {
    const intervalos = [];
    if (!slots || slots.length === 0) return [];
    let inicioAtual = slots[0];
    let ultimoSlot = slots[0];
    for (let i = 1; i < slots.length; i++) {
      if (this.saoConsecutivos(ultimoSlot, slots[i])) {
        ultimoSlot = slots[i];
      } else {
        intervalos.push({ inicio: inicioAtual, fim: this.adicionar15Min(ultimoSlot) });
        inicioAtual = slots[i];
        ultimoSlot = slots[i];
      }
    }
    intervalos.push({ inicio: inicioAtual, fim: this.adicionar15Min(ultimoSlot) });
    return intervalos;
  }

  private saoConsecutivos = (t1: string, t2: string) => (this.toMinutes(t2) - this.toMinutes(t1)) === 15;
  private adicionar15Min = (t: string) => {
    let m = this.toMinutes(t) + 15;
    return `${Math.floor(m/60).toString().padStart(2,'0')}:${(m%60).toString().padStart(2,'0')}`;
  }
  private toMinutes = (t: string) => { const [h,m] = t.split(':').map(Number); return h*60+m; };
  private converterDiaNomeParaEnum = (n: string) => ({'Domingo':'DOM','Segunda':'SEG','Terça':'TER','Quarta':'QUA','Quinta':'QUI','Sexta':'SEX','Sábado':'SAB'}[n] || 'SEG');
  private converterEnumParaDiaNome = (e: string) => ({'DOM':'Domingo','SEG':'Segunda','TER':'Terça','QUA':'Quarta','QUI':'Quinta','SEX':'Sexta','SAB':'Sábado'}[e] || 'Segunda');

  private preencherSlotsNoIntervalo(dia: string, inicio: string, fim: string) {
    let minA = this.toMinutes(inicio.substring(0,5));
    const minF = this.toMinutes(fim.substring(0,5));
    if (!this.horariosConfigurados[dia]) this.horariosConfigurados[dia] = [];
    while (minA < minF) {
      const h = `${Math.floor(minA/60).toString().padStart(2,'0')}:${(minA%60).toString().padStart(2,'0')}`;
      if (!this.horariosConfigurados[dia].includes(h)) this.horariosConfigurados[dia].push(h);
      minA += 15;
    }
  }

  inicializarDatas() {
    this.dataVisualizacaoAgenda = new Date().toISOString().split('T')[0];
  }

  carregarAgendaDoProfissional() {
    if (!this.usuarioLogado?.id || !this.dataVisualizacaoAgenda) return;
    this.agendamentoService.buscarAgendaDoDia(this.usuarioLogado.id, this.dataVisualizacaoAgenda).subscribe({
      next: (dados) => {
        this.agendaProfissional = dados.sort((a: any, b: any) => a.dataHora.localeCompare(b.dataHora));
        this.cdr.detectChanges();
      }
    });
  }

  toggleDiaExpandido(dia: string) { this.diaExpandido = (this.diaExpandido === dia) ? null : dia; }

  gerarSlotsConfiguracao() {
    for (let h = 8; h <= 18; h++) {
      for (let m = 0; m < 60; m += 15) {
        this.slotsHorariosConfig.push(`${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`);
      }
    }
  }

  toggleHorarioConfig(dia: string, horario: string) {
    if (!this.horariosConfigurados[dia]) this.horariosConfigurados[dia] = [];
    const idx = this.horariosConfigurados[dia].indexOf(horario);
    idx > -1 ? this.horariosConfigurados[dia].splice(idx, 1) : this.horariosConfigurados[dia].push(horario);
  }

  ehHorarioAtivo(dia: string, horario: string): boolean { return this.horariosConfigurados[dia]?.includes(horario) || false; }

  carregarAreas() {
    this.agendamentoService.listarAreas().subscribe(d => { this.areas = d; this.cdr.detectChanges(); });
  }
}