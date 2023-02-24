package hw2;


public class BathTest implements Runnable{
	static FairUnifanBathroom q = new FairUnifanBathroom();
	String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
    public static void main(String[] args){
    	
    	BathTest t1 = new BathTest();
    	
        

    	t1.run();
    	
    	//System.out.println(q);
	
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
	for( int i = 0; i<4; i++) {
	q.enterBathroomOU();
	System.out.println(i + " OU fan enters");
	}
	
	for( int i = 0; i<3; i++) {
		q.enterBathroomUT();
		System.out.println(i + " UT fan enters");}
	
	for( int i = 0; i<4; i++) {
		q.leaveBathroomOU();
		System.out.println(i + " OU fan leaves");
		}
	
	
	for( int i = 0; i<3; i++) {
	q.enterBathroomUT();
	System.out.println(i + " UT fan enters");}
	
		
	}
	
}