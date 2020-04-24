import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {

        ReentrantLock locker = new ReentrantLock();
        CallCenter callCenter = new CallCenter(locker);

        for (int i = 0; i < 2; i++){

            callCenter.addOperator(new Operator(locker,callCenter,i+1));
        }
        callCenter.startAllOperators();

        for (int i = 0; i < 20; i++){

            new Client(i+1,locker, callCenter, new Random().nextInt(500)).start();
        }

    }


}
