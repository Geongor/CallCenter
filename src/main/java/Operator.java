import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Operator extends  Thread {

    ReentrantLock locker;
    private CallCenter callCenter;
    private int countToServe = 10;
    private Client currentClient;
    private BlockingQueue<Client> clientsQueue;
    private int id;


    private static final Logger log = Logger.getLogger(Operator.class);

    public Operator(ReentrantLock locker, CallCenter callCenter, int id){

        super("Operator " + id);
        clientsQueue = callCenter.getClientsQueue();
        this.locker = locker;
        this.callCenter = callCenter;
        this.id = id;
    }

    @Override
    public void run(){
        while (countToServe > 0) {
            locker.lock();
            try {

                currentClient = clientsQueue.poll(3, TimeUnit.SECONDS);
                if (currentClient != null){
                    currentClient.reply();
                    countToServe--;
                } else {
                    System.out.println("Оператор " + id + " Закончил рабочий день, т.к. клиентов больше нет");
                    break;
                }

            } catch (InterruptedException e) {
                log.error("InterruptedException in Operator.run()");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }finally {
                locker.unlock();
            }
        }
        if (countToServe == 0)
        System.out.println("Оператор " + id + " Закончил рабочий день, т.к. освоил дневную норму клиентов");
    }
}
