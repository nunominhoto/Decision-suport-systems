

public class main_program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		////////////////////Escolha da heurística a utilizar///////////////////////
		
		int flag = 2;
		/*
		 * flag = 0
		 * 
		 * Executa as duas heurísticas, sendo a 1ª a de mimimizar distância entre lojas, e em seguida a de minimizar a distância ao armazem fornecedor
		 * 
		 * flag = 1
		 * Heurística que minimiza a distância de cada loja ao armazem que a abastece
		 * 
		 * flag = 2
		 * Heurística que minimiza a distância entre lojas
		*/
		
		///////////////Variáveis dos parâmetros iniciais para o SA/////////////////
		int temperature = 3;
		double cooling_rate = 0.0000001;
		///////////////////////////////////////////////////////////////////////////
		
		
		//////////////////Variáveis para condições finais do SA////////////////////
		//temperatura mínima que o algoritmo pode atingir
		float min_temp = 1;		
		
		//nº máximo de iterações que podem ser feitas sem mudança da melhor solução
		int max_it = 99999999;	
		///////////////////////////////////////////////////////////////////////////
		
		
		/////////////Variável apenas utilizada para a primeira eurística///////////
		//número de iterações realizadas até haver uma troca de loja entre rotas
		int n_it_trocas = 100;	
		///////////////////////////////////////////////////////////////////////////

		SAD sistema = new SAD(temperature, cooling_rate, min_temp, max_it, n_it_trocas); 

		if(flag == 0) {
			
			sistema.optimize_routs(2);
			
			sistema.optimize_routs(1);
			
		}else if((flag == 1) || (flag ==2)){
			
			sistema.optimize_routs(flag);
			
		}
					
	}

}
