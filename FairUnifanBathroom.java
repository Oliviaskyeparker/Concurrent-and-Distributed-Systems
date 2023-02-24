//osp257


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class FairUnifanBathroom {
  private int fansInBathroom;
  private boolean utInBathroom;
  private int ticketNumber;
  private int prevTicketNumber;
  private ReentrantLock lock;
  private Condition utCondition;
  private Condition ouCondition;

  public FairUnifanBathroom() {
    this.fansInBathroom = 0;
    this.utInBathroom = false;
    this.ticketNumber = 0;
    this.prevTicketNumber = 0;
    this.lock = new ReentrantLock();
    this.utCondition = lock.newCondition();
    this.ouCondition = lock.newCondition();
  }

  public void enterBathroomUT() {
    lock.lock();
    try {
      int myTicket = ticketNumber+1;
      ticketNumber=ticketNumber+1;
      
      while (fansInBathroom == 7 || (!utInBathroom && fansInBathroom > 0) || myTicket != prevTicketNumber+1) {
        utCondition.await();
      }
      fansInBathroom++;
      utInBathroom = true;
      prevTicketNumber=myTicket;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void enterBathroomOU() {
    lock.lock();
    try {
    	int myTicket = ticketNumber+1;
        ticketNumber=ticketNumber+1;
      while (fansInBathroom == 7 || (utInBathroom && fansInBathroom > 0) || myTicket != prevTicketNumber+1) {
        ouCondition.await();
      }
      fansInBathroom++;
      utInBathroom = false;
      prevTicketNumber=myTicket;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void leaveBathroomUT() {
    lock.lock();
    try {
      fansInBathroom--;
      if (fansInBathroom == 0) {
    	  utInBathroom = false;
          ouCondition.signalAll();
      }
      //utInBathroom = false;
      //ouCondition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void leaveBathroomOU() {
    lock.lock();
    try {
      fansInBathroom--;
      if (fansInBathroom == 0) {
    	  utInBathroom = true;
          utCondition.signalAll();
      }
      
    } finally {
      lock.unlock();
    }
  }
}
