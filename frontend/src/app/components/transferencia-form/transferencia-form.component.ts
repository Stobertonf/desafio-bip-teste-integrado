import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-transferencia-form',
  templateUrl: './transferencia-form.component.html',
  styleUrls: ['./transferencia-form.component.css']
})
export class TransferenciaFormComponent implements OnInit {
  transferenciaForm: FormGroup;
  beneficios: Beneficio[] = [];
  mensagem = '';
  erro = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService
  ) {
    this.transferenciaForm = this.fb.group({
      origemId: ['', Validators.required],
      destinoId: ['', Validators.required],
      valor: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.carregarBeneficios();
  }

  carregarBeneficios(): void {
    this.beneficioService.listarTodos().subscribe({
      next: (data) => {
        this.beneficios = data;
      },
      error: (err) => {
        console.error('Erro ao carregar benefícios', err);
      }
    });
  }

  onSubmit(): void {
    if (this.transferenciaForm.valid) {
      this.loading = true;
      this.mensagem = '';
      this.erro = '';

      const request = {
        origemId: Number(this.transferenciaForm.value.origemId),
        destinoId: Number(this.transferenciaForm.value.destinoId),
        valor: Number(this.transferenciaForm.value.valor)
      };

      this.beneficioService.transferir(request).subscribe({
        next: (response) => {
          this.mensagem = response.mensagem;
          this.loading = false;
          this.transferenciaForm.reset();
          this.carregarBeneficios();
        },
        error: (err) => {
          this.erro = err.error?.erro || 'Erro ao realizar transferência';
          this.loading = false;
          console.error(err);
        }
      });
    }
  }

  formatarValor(valor: number): string {
    if (valor === undefined || valor === null) return '0,00';
    return new Intl.NumberFormat('pt-BR', { 
      minimumFractionDigits: 2, 
      maximumFractionDigits: 2 
    }).format(valor);
  }
}
