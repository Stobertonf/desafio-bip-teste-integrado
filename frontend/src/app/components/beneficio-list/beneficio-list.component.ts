import { Component, OnInit } from '@angular/core';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-beneficio-list',
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.css']
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  loading = false;
  error = '';

  constructor(private beneficioService: BeneficioService) { }

  ngOnInit(): void {
    this.carregarBeneficios();
  }

  carregarBeneficios(): void {
    this.loading = true;
    this.beneficioService.listarTodos().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar benefícios';
        this.loading = false;
        console.error(err);
      }
    });
  }

  deletar(id: number): void {
    if (confirm('Tem certeza que deseja deletar este benefício?')) {
      this.beneficioService.deletar(id).subscribe({
        next: () => {
          this.carregarBeneficios();
        },
        error: (err) => {
          this.error = 'Erro ao deletar benefício';
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
