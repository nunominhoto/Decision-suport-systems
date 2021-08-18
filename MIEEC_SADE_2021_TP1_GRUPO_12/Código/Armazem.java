import java.util.ArrayList; // import the ArrayList clas


public class Armazem implements Entity, Cloneable{
	private float x_coord=0;
	private float y_coord=0;
	private int id=0;
	private int supply=0;
	
	private ArrayList<Loja> Lojas_Servidas = new ArrayList<Loja>();
	
	public Armazem(int id, float x, float y, int supply) {
		
		this.set_id(id);
		this.set_x_coord(x);
		this.set_y_coord(y);
		this.set_n_products(supply);
		
	}
	
	private String stringify_lojas() {
		String aux = new String();
		aux = "[";
		for (int i = 0; i < Lojas_Servidas.size(); i++) {
			aux += Lojas_Servidas.get(i).get_id();
			if(i != Lojas_Servidas.size()-1) {
				aux += "; ";
			}
		}
		aux += "]";
		return aux;
	}

	@Override
	public float get_x_coord() {
		return this.x_coord;
	}

	@Override
	public float get_y_coord() {
		return this.y_coord;
	}

	@Override
	public int get_id() {
		return this.id;
	}

	@Override
	public int get_n_products() {
		return this.supply;
	}

	@Override
	public void set_x_coord(float x) {
		this.x_coord = x;
	}

	@Override
	public void set_y_coord(float y) {
		this.y_coord = y;
	}

	@Override
	public void set_id(int id) {
		this.id = id;
	}

	@Override
	public void set_n_products(int n) {
		this.supply = n;
	}

	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}
	
	public void set_lista_lojas_servidas(ArrayList<Loja> list) {
		
		this.Lojas_Servidas = list;
		
	}
	
	public ArrayList<Loja> get_lista_lojas_servidas(){
		return this.Lojas_Servidas;
	}
	
	public void add_loja(Loja nova) {
		
		this.Lojas_Servidas.add(nova);
		
	}
	
	public double calc_dist (Loja l)
	{
		float x_dist = Math.abs(this.get_x_coord()-l.get_x_coord());
		float y_dist = Math.abs(this.get_y_coord()-l.get_y_coord());
		
		return Math.sqrt(Math.pow(x_dist,2)+Math.pow(y_dist,2));
	}
		
	@Override
	public String toString() {
		return "[Armazem " + this.id + "]: x = " + this.x_coord + "; y = " +this.y_coord + "   Supply: " + this.getSupply() + "  lojas: " + this.stringify_lojas();
	}
	
}
