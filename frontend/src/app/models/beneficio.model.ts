export interface Beneficio {
  id?: number;
  beneficiarioId: number;
  beneficiarioNome?: string;
  tipo: string;
  valor: number;
  saldo?: number;
  dataConcessao: string;
}

export interface TransferenciaRequest {
  origemId: number;
  destinoId: number;
  valor: number;
}

export interface TransferenciaResponse {
  mensagem: string;
  status: string;
}
