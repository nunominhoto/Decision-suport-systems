import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList; // import the ArrayList clas
import java.util.Scanner;
import java.util.StringTokenizer;


public class SAD {
	
	
	//Espaço destinado a guardar os path os ficheiros onde estão os dados para cada um//
	/*
	 * nuno: C:\\Users\\nuno-\\Desktop\\BrunerZ\\Faculdade\\4o_ano\\2º_Semestre\\SADE\\1o trabalho de grupo\\código\\SAD\\src\\
	 * pedro: C:\Users\pedro\OneDrive\Ambiente de Trabalho\Universidade\SADE\sade-1-trabalhp\Código\\
	 * resende: C:\Users\Resende\Downloads\Faculdade\SADE\Lab1\sade-1-trabalhp\Código\\
	 */
	
	private String filepath = "C:\\\\Users\\\\nuno-\\\\Desktop\\\\BrunerZ\\\\Faculdade\\\\4o_ano\\\\2º_Semestre\\\\SADE\\\\1o trabalho de grupo\\\\código\\\\SAD\\\\src\\\\";

	private ArrayList<Loja> Lojas = new ArrayList<Loja>();
	private ArrayList<Armazem> Armazens = new ArrayList<Armazem>();
	private double[][] dist_ll;
	private double[][] dist_al;	
	private int temp;
	private double cr;
	private float min_temp;
	private int max_it;
	private int n_it_trocas;
	
	
	//////inicialização do sistema de decisão
	public SAD(int temperature, double cooling, float min_temp_in, int max_it_in, int n_it_trocas_in) {
		this.ler_ficheiro_Armazens();
		this.ler_ficheiro_Lojas();
		
		this.set_dist_lojas();	
		this.set_dist_armazens();
		
		this.temp = temperature;
		this.cr = cooling;
		this.max_it = max_it_in;
		this.min_temp = min_temp_in;
		this.n_it_trocas = n_it_trocas_in;
		
	}
	
	/////função de optimização
	public void optimize_routs(int flag) {
		
		if(flag == 1) {
			
			
			System.out.println("Simulated Annealing iniciado com:");
			System.out.println("temperatura: " + this.temp + ", cooling rate: " + this.cr);
			System.out.println("Critérios de paragem do algoritmo: ");
			System.out.println("Temperatura < " + min_temp + " ou número maximo de iterações sem mudança da melhor solução: " + this.max_it);	
			System.out.println("Heurística: distância mínima entre lojas e armazens");
			System.out.println("1º agrupa as lojas ao armazem que estiver mais perto destas");
			System.out.println("2º mudar a ordem das rotas utilizando o algoritmo Simulated Annealing");
			System.out.println("3º a cada " + this.n_it_trocas +" iterações troca uma loja aleatória de um armazem aleatório para a rota de outro armazem aleatório");
			
			this.associa_lojas_armazens();
						
			Simulated_Annealing S_A = new Simulated_Annealing(this.temp, this.cr, this.Armazens, this.Lojas,this.min_temp, this.max_it, this.n_it_trocas);
					
			S_A.optimize_reorder();
			
			/*this.print_associações(S_A.armazens);
			System.out.println("initial fitness: " + S_A.find_fitness(S_A.armazens));*/
			
			this.print_associações(S_A.get_best_sol());
			//System.out.println("best solution: " + S_A.best_solution_fitness);
			System.out.println("distância percorrida: " + S_A.find_fitness(S_A.get_best_sol()));
			
			
			
		}else if(flag == 2) {
			
			System.out.println("Simulated Annealing iniciado com:");
			System.out.println("temperatura: " + this.temp + ", cooling rate: " + this.cr);
			System.out.println("Critérios de paragem do algoritmo: ");
			System.out.println("Temperatura < " + min_temp + " ou número maximo de iterações sem mudança da melhor solução: " + this.max_it);	
			System.out.println("Heurística: distância mínima entre lojas consecutivas");
			System.out.println("1º escolher uma loja alatória da lista");
			System.out.println("2º encontrar o armazem mais perto desta");
			System.out.println("3º preencher a rota com as lojas mais próximas da ultima loja adicionada, até o armazem ficar sem capacidade de fornecimento");
			
			Simulated_Annealing S_A = new Simulated_Annealing(this.temp, this.cr, this.Armazens, this.Lojas, this.min_temp, this.max_it, this.n_it_trocas);

			S_A.optimize_min_dist();
			
			this.print_associações(S_A.get_best_sol());
			//System.out.println("best solution final: " + S_A.best_solution_fitness);
			System.out.println("distância percorrida: " + S_A.find_fitness(S_A.get_best_sol()));
			
			
		}
		
		
		
	}
	
	////funções que lêm os ficheiros
	public void ler_ficheiro_Armazens() {
		
		int id;
		float x;
		float y;
		int n;
		
		try {
			
		      File Arm = new File(filepath + "Caso 01 [dados (armazens)].txt");
		      Scanner reader_arm = new Scanner(Arm);
		      String data = null;
		      data = reader_arm.nextLine();
		      while (reader_arm.hasNextLine()) {
		        data = reader_arm.nextLine();
		        //System.out.println(data);
		        StringTokenizer tokenizer = new StringTokenizer(data, ";");
		        
				id = Integer.parseInt(tokenizer.nextToken());			
				n = Integer.parseInt(tokenizer.nextToken());
				x = Float.parseFloat(tokenizer.nextToken());
				y = Float.parseFloat(tokenizer.nextToken());
        			
				Armazens.add(new Armazem(id, x, y, n));
		          	       		        
		      }
		      
		      reader_arm.close();
		      
		      //System.out.println(Armazens.size() + " armazens");
		      		      
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }

	}
	
	public void ler_ficheiro_Lojas() {
		
		int id;
		float x;
		float y;
		int n;
	
	try {
		File Loj = new File(filepath + "Caso 01 [dados (lojas)].txt");
	      Scanner reader_lojas = new Scanner(Loj);
	      String data = null;
	      data = reader_lojas.nextLine();
	      while (reader_lojas.hasNextLine()) {
	        data = reader_lojas.nextLine();
	        //System.out.println(data);
	        StringTokenizer tokenizer = new StringTokenizer(data, ";");
	        
			id = Integer.parseInt(tokenizer.nextToken());					
			x = Float.parseFloat(tokenizer.nextToken());
			y = Float.parseFloat(tokenizer.nextToken());
			n = Integer.parseInt(tokenizer.nextToken());
  			
			Lojas.add(new Loja(id, x, y, n));
	                 		        
	      }
	      
	      reader_lojas.close();
	      
	      //System.out.println(Lojas.size() + " lojas");
		
	}	catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		
	}
	
	////Funções que associam a cada loja o armazem mais proximo para a servir
	public void associa_lojas_armazens() {
		double dist_min;
		int armz_id = 0;
		for (Loja loja : this.Lojas) {
			dist_min=500000000;
			for (Armazem armazem : this.Armazens) {
				
				if((this.dist_al[armazem.get_id()-1][loja.get_id()-1] < dist_min) && (armazem.getSupply() >= loja.getDemand())) {
					dist_min = this.dist_al[armazem.get_id()-1][loja.get_id()-1];
					armz_id = armazem.get_id();
				}
				
			}
			
			set_loja_armazem(armz_id-1, loja.get_id()-1);
			
		}	
		
	}
	
	public void set_loja_armazem(int armazem, int loja) {
		if(armazem > -1) {
		Armazens.get(armazem).add_loja(Lojas.get(loja));
		Armazens.get(armazem).setSupply(Armazens.get(armazem).getSupply()-Lojas.get(loja).getDemand());
		Lojas.get(loja).set_armazem_fornecedor(armazem);
		}
	}
		
	public ArrayList<Loja> getLojas() {
		return Lojas;
	}
	
	public ArrayList<Armazem> getArmazens() {
		return Armazens;
	}
	
	////calcula as tabelas que contêm as distâncias entre as várias lojas, e entre lojas e armazens
	public void set_dist_lojas ()
	{
		
		if (this.dist_ll == null) {
			this.dist_ll = new double[Lojas.size()][Lojas.size()];
		}
		
		
		for(Loja l1 : this.Lojas) {
			for(Loja l2 : this.Lojas) {
				this.dist_ll[l1.get_id()-1][l2.get_id()-1] = l1.calc_dist(l2);
			}
		}
	}
	
	public void set_dist_armazens ()
	{
		
		if (this.dist_al == null) {
			this.dist_al = new double[Armazens.size()][Lojas.size()];
		}
		
		
		for(Armazem a1 : this.Armazens) {
			for(Loja l2 : this.Lojas) {
				this.dist_al[a1.get_id()-1][l2.get_id()-1] = a1.calc_dist(l2);
			}
		}
	}
		
	//////funções de print de variáveis
	public void print_lojas() {
		for (Loja l : Lojas) {
			System.out.println(l);
		}
	}
	
	public void print_armazens() {
		for (Armazem a : Armazens) {
			System.out.println(a);
		}
	}
	
	public void print_armazens_non_supply() {
		for (Armazem a : Armazens) {
			if(a.get_lista_lojas_servidas().isEmpty()) {
				System.out.println(a);
			}
		}
	}
	
	public void print_lojas_non_supply() {
		for (Loja l : Lojas) {
			if(l.get_id_armazem_fornecedor()==-1) {
				System.out.println(l);
			}
		}
	}

	public void print_associações(ArrayList <Armazem> a_list) {
		
		int count=0;
		
		for (Armazem armazem : a_list) {
			count += armazem.get_lista_lojas_servidas().size();
			System.out.print("Armazem: " + armazem.get_id() + " serve as lojas: ");
			for (Loja loja : armazem.get_lista_lojas_servidas()) {
				System.out.print(loja.get_id() + ", ");
			}
			
			System.out.println("stock restante: " + armazem.getSupply());
		}
		
		System.out.println("nº de lojas servidas: " + count);
		
		
	}
	
	public void print_dist_ll() {
		for(int i=1; i < this.dist_ll.length; i++) {
			for( int j=1; j<  this.dist_ll[i].length; j++) {
				System.out.print(this.dist_ll[i][j]+" | ");
			}
			System.out.println();
		}
	}
	
	public void print_dist_al() {
		for(int i=1; i < this.dist_al.length; i++) {
			for( int j=1; j<  this.dist_al[i].length; j++) {
				System.out.print(this.dist_al[i][j]+" | ");
			}
			System.out.println();
		}
	}
	
	
}
