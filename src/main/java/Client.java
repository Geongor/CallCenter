import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends Thread {

    int id;
    private volatile boolean resolved = false;
    private volatile boolean onCall = false;
    private ReentrantLock locker;
    private CallCenter callCenter;
    private int timeToServe;
    private BlockingQueue<Client> clientsQueue;

    private static final Logger log = Logger.getLogger(Client.class);

    public Client(int id, ReentrantLock locker, CallCenter callCenter, int timeToServe) {
        super("Client "+id);
        this.id = id;
        this.locker = locker;
        this.callCenter = callCenter;
        this.timeToServe = timeToServe;
        clientsQueue = callCenter.getClientsQueue();
    }

    @Override
    public void run() {

        try {
            sleep(new Random().nextInt(2000));
            callCenter.Call(this);

            System.out.println("Клиент " + id + " Вошел в очередь");
        } catch (InterruptedException e) {
            log.error("InterruptedException in Client.run() (1)");
            e.printStackTrace();
        }

        while (!resolved){
            try {
                sleep(100);
                if (Math.random() > 0.95){
                    if (!onCall && !resolved) {
                        callCenter.BreakCall(this);
                        System.out.println("Клиент " + id + " покинул очередь");
                        if (Math.random() > 0.7) {
                            callCenter.Call(this);
                            System.out.println("Клиент " + id + " Вернулся в очередь");

                        } else {
                           break;
                        }
                    }
                }
            } catch (InterruptedException e){
                log.error("InterruptedException in Client.run() (2)");
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    public void reply() {

        try {
            onCall = true;
            sleep(timeToServe);
            resolved = true;
            System.out.println("Клиент " + id + " Вопрос решен!");

        } catch (InterruptedException e) {
            log.error("InterruptedException in Client.reply()");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

    }


    public int getClientId() {
        return id;
    }
}
