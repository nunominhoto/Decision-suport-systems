

public class main_program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		////////////////////Escolha da heur�stica a utilizar///////////////////////
		
		int flag = 2;
		/*
		 * flag = 0
		 * 
		 * Executa as duas heur�sticas, sendo a 1� a de mimimizar dist�ncia entre lojas, e em seguida a de minimizar a dist�ncia ao armazem fornecedor
		 * 
		 * flag = 1
		 * Heur�stica que minimiza a dist�ncia de cada loja ao armazem que a abastece
		 * 
		 * flag = 2
		 * Heur�stica que minimiza a dist�ncia entre lojas
		*/
		
		///////////////Vari�veis dos par�metros iniciais para o SA/////////////////
		int temperature = 3;
		double cooling_rate = 0.0000001;
		///////////////////////////////////////////////////////////////////////////
		
		
		//////////////////Vari�veis para condi��es finais do SA////////////////////
		//temperatura m�nima que o algoritmo pode atingir
		float min_temp = 1;		
		
		//n� m�ximo de itera��es que podem ser feitas sem mudan�a da melhor solu��o
		int max_it = 99999999;	
		///////////////////////////////////////////////////////////////////////////
		
		
		/////////////Vari�vel apenas utilizada para a primeira eur�stica///////////
		//n�mero de itera��es realizadas at� haver uma troca de loja entre rotas
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
