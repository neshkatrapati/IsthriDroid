package apps.id;

public class Transaction {

	private int id;
	private int gid;
	private int tid;
	private int qty;
	private double cost;
	private double icost;
	public double getIcost() {
		return icost;
	}
	public void setIcost(double icost) {
		this.icost = icost;
	}
	private double total;
	private double paid;
	private int rqty;
	
	
	public int getRqty() {
		return rqty;
	}
	public void setRqty(int rqty) {
		this.rqty = rqty;
	}
	public double getPaid() {
		return paid;
	}
	public void setPaid(double paid) {
		this.paid = paid;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	private String cdate;
	
	
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String hasPaid(){
		
		if (this.paid == this.total){
			
			return  "Paid";
		}
		return Double.toString(this.total - this.paid) + " Rs";
	}
	

}
