

import java.util.concurrent.Semaphore;

//osp257



/* Use only semaphores to accomplish the required synchronization */
public class SemaphoreCyclicBarrier implements CyclicBarrier {
	private int parties;
    private Semaphore semaphore;
    private Semaphore activationSemaphore;
    private boolean active;
    private int index;


 // TODO Add other useful variables

 public SemaphoreCyclicBarrier(int parties) {
	 this.parties = parties;
     this.semaphore = new Semaphore(0);
     this.activationSemaphore = new Semaphore(1);
     this.active = true;
     this.index=0;
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
	 if (active==true) {
         semaphore.acquire();
         index = semaphore.availablePermits();
         int count = parties - index - 1;
         if (count == (parties-1)) {
             semaphore.release(parties);
         }
         return count;
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
	 if(active==false) {
	 activationSemaphore.acquire();
     active = true;
     semaphore = new Semaphore(0);
     activationSemaphore.release(); }
 }

 /*
  * This method deactivates the cyclic barrier.
  * It also releases any waiting threads
  */
 public void deactivate() throws InterruptedException {
	 activationSemaphore.acquire();
     active = false;
     semaphore.release(parties);
     activationSemaphore.release();
 }
}