
public class Loja implements Entity{
	
	private float x_coord=0;
	private float y_coord=0;
	private int id=0;
	private int demand=0;
	private int armazem_fornecedor = -1;

	
	public Loja(int id, float x, float y, int demand) {
		
		this.set_id(id);
		this.set_x_coord(x);
		this.set_y_coord(y);
		this.set_n_products(demand);
		
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
		return this.demand;
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
		this.demand = n;
	}

	public void set_armazem_fornecedor(int id) {
		
		this.armazem_fornecedor = id;
		
	}

	public int get_id_armazem_fornecedor() {
		return this.armazem_fornecedor;
	}
	
	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}



	public double calc_dist (Loja l)
	{
		float x_dist = Math.abs(this.get_x_coord()-l.get_x_coord());
		float y_dist = Math.abs(this.get_y_coord()-l.get_y_coord());
		
		return Math.sqrt(Math.pow(x_dist,2)+Math.pow(y_dist,2));
	}
	
	@Override
	public String toString() {
		return "[Loja " + this.id + "]: x = " + this.x_coord + "; y = " +this.y_coord + " demand: " + this.getDemand() + " armazem: " + this.armazem_fornecedor;
	}
	
}
