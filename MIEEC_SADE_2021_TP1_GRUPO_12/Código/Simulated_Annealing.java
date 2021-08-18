import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulated_Annealing {

	private double temp; //temperatura
	private double cr; //cooling rate

	public ArrayList <Armazem> current_solution;
	public ArrayList <Armazem> best_solution;
	public ArrayList <Armazem> new_solution;	
	
	private ArrayList <Loja> lojas;
	public ArrayList <Armazem> armazens;
	
	public double best_solution_fitness=0;
	public double current_solution_fitness=0;
	public double new_solution_fitness=0;
	
	private float min_temp;
	private int max_it;
	private int n_it_trocas;
		
	public Simulated_Annealing(double temp, double cr, ArrayList <Armazem> list_armazem, ArrayList <Loja> list_lojas, float min_temp_in, int max_it_in, int n_it_trocas_in)
    {
        this.temp = temp;
        this.cr = cr;
        this.lojas = new ArrayList<Loja>(list_lojas);
        this.armazens = new ArrayList<Armazem>(list_armazem);
        
        this.min_temp = min_temp_in;
        this.max_it = max_it_in;
        this.n_it_trocas = n_it_trocas_in;
        
    }
	
	
	////////////////////////função que encontra a fitness(ditância percorrida) de cada solução ///////////////////
	public double find_fitness(ArrayList<Armazem> list_armazens) {
		
		float dist = 0, temp_dist = 0, d=0;
		
		int i=0;
		
		for (Armazem armazem : list_armazens) {
			temp_dist = 0;
			if(armazem.get_lista_lojas_servidas().size() > 0) {
				for (i = 0; i < armazem.get_lista_lojas_servidas().size(); i++) {
					if(i==0) {
						d = (float) armazem.calc_dist(armazem.get_lista_lojas_servidas().get(i));
						//System.out.println("armz loja 1: " + d);
					}else if(i != armazem.get_lista_lojas_servidas().size()){
						d = (float) armazem.get_lista_lojas_servidas().get(i).calc_dist(armazem.get_lista_lojas_servidas().get(i-1));
						//System.out.println(d);
					}
					dist = dist + d;
					temp_dist = temp_dist + d;
				}
				d = (float) armazem.calc_dist(armazem.get_lista_lojas_servidas().get(armazem.get_lista_lojas_servidas().size()-1));
				
				dist = dist + d;
				temp_dist = temp_dist + d;
			}
			
			//System.out.println("Armazem " + armazem.get_id() + " dist_percorrida: " + temp_dist);
			
		}
		
		return dist;
		
	}

	////////////////funções que encontram novas soluções para a 1ª heurística///////////////////////////////////
	private ArrayList<Armazem> find_new_solution_switch_stores(ArrayList<Armazem> current_sol) {
		
		ArrayList <Armazem> new_sol = new ArrayList <Armazem>(current_sol);
		
		Random rand = new Random(); //objeto que vai gerar os valores aleatórios 
		
		int r_a1=0, r_a2=0;    ////variaveis para armazens aleatórios
		int r_l2;    ////variaveis para as lojas em posições aleatórias
		int supply1, supply2; //variaveis auxiliares para calculos com supply
		
		//variaveis auxiliares para ajudarem na troca
		Loja l2;
		Armazem a1, a2;
		
		while(true) {
			//encontrar 2 numeros aleatórios diferentes para os armazens
			do {
				
				r_a1 = rand.nextInt(new_sol.size());
				r_a2 = rand.nextInt(new_sol.size());
				
			} while (r_a1 == r_a2);
			
			if(new_sol.get(r_a2).get_lista_lojas_servidas().size() > 0) {
				
				//encontrar 1 numeros aleatórios para a loja a trocar
				
				if(new_sol.get(r_a2).get_lista_lojas_servidas().size() > 1){
					r_l2 = rand.nextInt(new_sol.get(r_a2).get_lista_lojas_servidas().size());
				}else {
					r_l2 = 0;
				}
				
				//associa as lojas e armazens aleatórios encontrados às variáveis auxiliares
				a1 = new_sol.get(r_a1);
				a2 = new_sol.get(r_a2);

				l2 = a2.get_lista_lojas_servidas().get(r_l2);
				
				supply1 = a1.getSupply();
				supply2 = a2.getSupply() + l2.getDemand();
				
				if((supply1 >= l2.getDemand()) && (supply1 > 0)) {
					
					
					//////troca duas lojas////
					new_sol.get(r_a1).get_lista_lojas_servidas().add(l2);
					new_sol.get(r_a2).get_lista_lojas_servidas().remove(l2);
					
					new_sol.get(r_a1).setSupply(supply1 - l2.getDemand());
					new_sol.get(r_a2).setSupply(supply2);
					
					//System.out.println("Troca de lojas");
					
					//return new_sol;
					return new_sol;
					
				}
			
			}
			
		}
		
		
	}
	
	private ArrayList<Armazem> find_new_solution_store_reorder(ArrayList<Armazem> current_sol){
		
		ArrayList <Armazem> new_sol = new ArrayList <Armazem>(current_sol);
		
		Random rand = new Random(); //objeto que vai gerar os valores aleatórios 
		
		int r_a, r_l1, r_l2;
		
		boolean done = false;
		
		while(done != true) {
			
			r_a = rand.nextInt(new_sol.size());
			
			if(new_sol.get(r_a).get_lista_lojas_servidas().size()>1) {
				
				r_l1 = rand.nextInt(new_sol.get(r_a).get_lista_lojas_servidas().size());
				
				do {
					r_l2 = rand.nextInt(new_sol.get(r_a).get_lista_lojas_servidas().size());
				} while (r_l1 == r_l2);
				
				Collections.swap(new_sol.get(r_a).get_lista_lojas_servidas(), r_l1, r_l2);
				
				//System.out.println("found new");
				
				done = true;
					
			}
			
		}
			
		return new_sol;
		
	}
	
	///////////////função utilizada para encontrar soluções para a segunda heurística/////////////////
	public ArrayList<Armazem> find_new_solution_closest(){
		
		@SuppressWarnings("unchecked")
		ArrayList <Loja> copia_lojas = (ArrayList<Loja>) new ArrayList<Loja>(this.lojas).clone();
		ArrayList <Armazem> new_sol = this.copia_armazens(armazens);	
		
		

		Random rand = new Random();
		
		Loja loja_aux;
		
		int a, l2;
	
		
		/////enquanto houver lojas sem associações a armazens, vai percorrer o loop para as associar
		while(copia_lojas.size()>0) {
			
			int l = rand.nextInt(copia_lojas.size());
			
			loja_aux = copia_lojas.get(l);
			
			//encontra o armazem mais perto e adiciona essa loja à rota desse armazem
			a = find_armazem_perto(l, copia_lojas, new_sol);

			new_sol.get(a).add_loja(new Loja(loja_aux.get_id(), loja_aux.get_x_coord(), loja_aux.get_y_coord(), loja_aux.getDemand()));	
			new_sol.get(a).setSupply(new_sol.get(a).getSupply() - loja_aux.getDemand());
			copia_lojas.remove(l);
		
			
			if(copia_lojas.size() > 0) {
				
				while(new_sol.get(a).getSupply() > 0) {
					
					l2 = find_loja_perto(loja_aux, copia_lojas);
							
					if(l2 == 100) {
						//não encontra nenhuma loja nova perto
						break;
					
					}else if(new_sol.get(a).getSupply() > copia_lojas.get(l2).getDemand()){
						
						loja_aux = copia_lojas.get(l2);
						
						new_sol.get(a).add_loja(new Loja(loja_aux.get_id(), loja_aux.get_x_coord(), loja_aux.get_y_coord(), loja_aux.getDemand()));
						
						new_sol.get(a).setSupply(new_sol.get(a).getSupply() - copia_lojas.get(l2).getDemand());
						copia_lojas.remove(l2);
							
					}else {
						break;
					}
					
				}
				
			}
			
		}
		
		return new_sol;
		
	}
	
	private int find_armazem_perto(int l, ArrayList <Loja> copia_lojas, ArrayList <Armazem> new_sol) {
		
		double dist_max = 900000000;
		
		int id_a = 0, i = 0;
		
		for (Armazem armazem : new_sol) {	

			if((armazem.calc_dist(copia_lojas.get(l)) < dist_max) && (armazem.getSupply() >= copia_lojas.get(l).getDemand()) && (armazem.get_lista_lojas_servidas().size() == 0)) {
				id_a = i;
				dist_max = armazem.calc_dist(copia_lojas.get(l));
			}
			
			i++;
		}
		
		return id_a;
		
	}
	
	private int find_loja_perto(Loja loja, ArrayList <Loja> copia_lojas) {
		
		double dist_max = 900000000;
		
		int aux = 100, i=0;;
		
		for (Loja loja2 : copia_lojas) {
			if(loja.calc_dist(loja2) < dist_max) {
				dist_max = loja.calc_dist(loja2);
				aux = i;
			}
			
			i++;
			
		}
		
		return aux;
		
	}
	
	////////////função criada para copiar as listas de armazens, uma vez que com o método clone não eram copiados devidamente/////////////////
	private ArrayList<Armazem> copia_armazens(ArrayList<Armazem> origin) {
		
		ArrayList<Armazem> copy = new ArrayList<Armazem>();
		
		int i=0;
		
		for (Armazem armazem : origin) {
			copy.add(new Armazem(armazem.get_id(), armazem.get_x_coord(), armazem.get_y_coord(), armazem.getSupply()));
			
			if(armazem.get_lista_lojas_servidas().size() > 0) {
				for (Loja loja : armazem.get_lista_lojas_servidas()) {
					copy.get(i).get_lista_lojas_servidas().add(new Loja(loja.get_id(), loja.get_x_coord(), loja.get_y_coord(), loja.getDemand()));
				}
			}
			
			i++;
			
		}
		
		return copy;
		
	}
	
	///////////Algoritmo de Simulated Annealing que utiliza a primeira heurística//////////////////////////
	public void optimize_reorder() {
		
        this.current_solution = this.copia_armazens(this.armazens);
        this.best_solution = this.copia_armazens(this.current_solution);
        this.best_solution_fitness = find_fitness(best_solution);
        this.current_solution_fitness = find_fitness(current_solution);
		
		int it = 0;
		
		long startTime = System.currentTimeMillis();
		
		Random r = new Random();
		
		int count = 0;
		
		double prob;	
		
		while(this.temp > this.min_temp && count < this.max_it) {
			
			
			this.new_solution = this.copia_armazens(this.current_solution);
			
			if(it == this.n_it_trocas) {
				
				//this.new_solution = this.find_new_solution_close_to_last(this.new_solution);
				this.new_solution = this.find_new_solution_switch_stores(this.new_solution);
				it=0;

			}
			
			this.new_solution = this.find_new_solution_store_reorder(this.new_solution);
	
			this.new_solution_fitness = this.find_fitness(this.new_solution);
			
			if(this.new_solution_fitness < this.current_solution_fitness) {
				
				//System.out.println("better");
				
				this.current_solution.clear();
				this.current_solution = this.copia_armazens(this.new_solution);
				this.current_solution_fitness = this.new_solution_fitness;
				
				//System.out.println("current: devem ser iguais: " + this.current_solution_fitness + " " + this.find_fitness(this.current_solution));
				
				
				if(this.current_solution_fitness < this.best_solution_fitness) {
					
					count = 0;
					this.best_solution.clear();
					this.best_solution = this.copia_armazens(this.current_solution);
					this.best_solution_fitness = this.current_solution_fitness;
					
					//System.out.println("best: devem ser iguais: " + this.best_solution_fitness + " " + this.find_fitness(this.best_solution));
						
				}
			}else {
				prob = Math.exp((this.current_solution_fitness - this.new_solution_fitness)/this.temp);
				
				if(prob > r.nextDouble())
                {
					this.current_solution.clear();
                    this.current_solution_fitness = this.new_solution_fitness;
                    this.current_solution = this.copia_armazens(this.new_solution);
                }
				
			}
			
            //update temperature
            this.temp = this.temp*(1.0 - this.cr);
            //System.out.println("Current temperature: " + String.valueOf(this.temp));
			count++;
			it++;
		}
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long elapsedMinutes = elapsedSeconds / 60;
		
		
		
		if(elapsedSeconds >= 60) {
			System.out.println("tempo decorrido: " + elapsedMinutes + "min");
		}else if(elapsedTime < 1000){
			System.out.println("tempo decorrido: " + elapsedTime + "ms");
		}else if(elapsedTime >= 1000) {
			System.out.println("tempo decorrido: " + elapsedSeconds + "s");
		}
		
		
			
	}
	
	///////////Algoritmo de Simulated Annealing que utiliza a segunda heurística//////////////////////////
	public void optimize_min_dist() {
		
		//ArrayList <Armazem> default_armazens = new ArrayList<Armazem>(current_solution);
		
		this.current_solution = this.copia_armazens(this.find_new_solution_closest());
		this.current_solution_fitness = find_fitness(current_solution);
		
		this.best_solution = this.copia_armazens(current_solution);
		this.best_solution_fitness = this.current_solution_fitness;
		
		long startTime = System.currentTimeMillis();
		
		Random r = new Random();
		
		int count = 0, it=0;
		
		double prob;	
				
		while(this.temp > this.min_temp && count < this.max_it) {
				
			this.new_solution = this.copia_armazens(this.current_solution);
			
			if(it == this.n_it_trocas) {
				
				//this.new_solution = this.find_new_solution_close_to_last(this.new_solution);
				this.new_solution = this.find_new_solution_switch_stores(this.new_solution);
				it=0;

			}
			
			this.new_solution = this.find_new_solution_store_reorder(this.new_solution);
			
			
			if(this.new_solution_fitness < this.current_solution_fitness) {
				
				//System.out.println("better");
				
				this.current_solution.clear();
				this.current_solution = this.copia_armazens(this.new_solution);
				this.current_solution_fitness = this.new_solution_fitness;
				
				//System.out.println("current: devem ser iguais: " + this.current_solution_fitness + " " + this.find_fitness(this.current_solution));
				
				
				if(this.current_solution_fitness < this.best_solution_fitness) {
					
					count = 0;
					this.best_solution.clear();
					this.best_solution = this.copia_armazens(current_solution);
					this.best_solution_fitness = this.current_solution_fitness;
					
					//System.out.println("best: devem ser iguais: " + this.best_solution_fitness + " " + this.find_fitness(this.best_solution));
						
				}
			}else {
				//the probability of changing to a worst solution increases with the temperature and decreases the more the new solution is worst than the current one
				prob = Math.exp((this.current_solution_fitness - this.new_solution_fitness)/this.temp);
				
				if(prob > r.nextDouble())
                {
					this.current_solution.clear();
                    this.current_solution_fitness = this.new_solution_fitness;
                    this.current_solution = this.copia_armazens(new_solution);
                }
				
			}
			
            //update temperature
            this.temp = this.temp*(1.0 - this.cr);
            //System.out.println("Current temperature: " + String.valueOf(this.temp));
			count++; 
			it++;

		}
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long elapsedMinutes = elapsedSeconds / 60;
		
		
		
		if(elapsedSeconds >= 60) {
			System.out.println("tempo decorrido: " + elapsedMinutes + "min");
		}else if(elapsedTime < 1000){
			System.out.println("tempo decorrido: " + elapsedTime + "ms");
		}else if(elapsedTime >= 1000) {
			System.out.println("tempo decorrido: " + elapsedSeconds + "s");
		}
		
		//System.out.println(this.best_solution);
		//System.out.println("fitness best: " + this.best_solution_fitness);
		
	}
	
	///////////////////////getters de acesso a variáveis/////////////////////////////////////
	public ArrayList<Armazem> get_best_sol(){
		return this.best_solution;
	}
	
	public double get_best_sol_fitness() {
		return this.best_solution_fitness;
	}

	
}

