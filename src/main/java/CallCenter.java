import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class CallCenter {


    private volatile BlockingQueue<Client> clientsQueue = new LinkedBlockingQueue<Client>();
    private List<Operator> operators = new ArrayList<Operator>();
    private ReentrantLock locker;

    public CallCenter(ReentrantLock locker){

        this.locker = locker;
    }


    public BlockingQueue<Client> getClientsQueue(){

        return clientsQueue;
    }
    public void Call(Client client){

        clientsQueue.offer(client);

    }

    public void BreakCall(Client client){

        if (clientsQueue.contains(client))
            clientsQueue.remove(client);
    }

    public void addOperator(Operator operator){

        operators.add(operator);
    }

    public  void  startAllOperators(){

        operators.forEach(Thread::start);
    }
}
