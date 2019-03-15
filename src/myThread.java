import DataTypes.Bus;

import java.io.IOException;
/**this is a client type thread two emulate multiple clients in the same machine
 * idea: to have multiple clients that will do the same thing or thou parameters to slightly
 * different while being flexible**/
//TODO rename to clientThread
public class myThread implements Runnable  {
    private String threadType;
    private int threadId;
    //constructor
    public myThread(String threadType , int threadId){
        this.threadType=threadType;this.threadId=threadId;
    }
    @Override
    /**this is what the thread will do when up and running**/
    public void run() {
        DataTypes.Bus bus = new DataTypes.Bus("b12"," "," ","patision"," ","d");
        NodeImple nodeImpl =new NodeImple("bill","localhost","Client",4325,"6DF8895738C9795DE942F2FC7A48E127");
        connectClient(nodeImpl, "localhost");
    }//end run
    public void connectClient(NodeImple bus, String ip){
        try {
            System.out.println("ip: " + ip+" port "+ 4201);
            Thread.sleep(100);
            new Client().startClient(ip, 4201,bus);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}//end class myThread
