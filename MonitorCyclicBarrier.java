
// osp257

/* Use only Java monitors to accomplish the required synchronization */
public class MonitorCyclicBarrier implements CyclicBarrier {

    private int parties;
    private int index;
    private boolean active;

    public MonitorCyclicBarrier(int parties) {
        this.parties = parties;
        this.index=parties-1;
        this.active = true;
        // TODO Add any other initialization statements
    }

    /*
     * An active CyclicBarrier waits until all parties have invoked
     * await on this CyclicBarrier. If the current thread is not
     * the last to arrive then it is disabled for thread scheduling
     * purposes and lies dormant until the last thread arrives.
     * An inactive CyclicBarrier does not block the calling thread. It
     * instead allows the thread to proceed by immediately returning.
     * Returns: the arrival index of the current thread, where index 0
     * indicates the first to arrive and (parties-1) indicates
     * the last to arrive.
     */
    public int await() throws InterruptedException {
        // TODO Implement this function
    	 if(active) {
    	int myIndex = index;
         index--;

         if(index >= 0){
        	 synchronized(this){
                 this.wait();}

         }
         else{
             index=parties - 1;
             
             synchronized(this){
            	    notifyAll();}

         
         }

         return myIndex;
     }
    	 return -1;
    }

    /*
     * This method activates the cyclic barrier. If it is already in
     * the active state, no change is made.
     * If the barrier is in the inactive state, it is activated and
     * the state of the barrier is reset to its initial value.
     */
    public void activate() throws InterruptedException {
        // TODO Implement this function
    	active = true;
    	index = parties- 1;
    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
    	active = false;
    	synchronized(this){
    	    notifyAll();}

    }
}