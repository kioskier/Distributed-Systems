import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class BrokerConnect extends Node implements Runnable {
    private String RemoteBrokerIp;
    private int RemoteBrokerPort;
    public BrokerConnect(String RemoteBrokerIp, int RemoteBrokerPort,int port){
        this.RemoteBrokerIp=RemoteBrokerIp;
        this.RemoteBrokerPort=RemoteBrokerPort;
        this.port=port;
    }
    public void run(){
        Myconnection();
    }
    public void Myconnection(){
        Socket socket = connect(RemoteBrokerIp,RemoteBrokerPort);
        try {
            System.out.println("Connected");
            ObjectInputStream  in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(in.readUTF());
            out.writeUTF("BrokerAdd");
            out.flush();
            out.writeUTF(Integer.toString(port));//einai 0 prepei na perastei alio
            out.flush();
            // m BrokerList.add(new Brocker(RemoteBrokerPort,RemoteBrokerIp));
            while (true){
                TimeUnit.SECONDS.sleep(10);
                out.writeUTF("ping");
                out.flush();
                System.out.println(in.readUTF());
            }
        } catch (IOException e) {
            System.out.println("No connection could be established");

            //e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**geter seter **/
    public String getRemoteBrokerIp() {
        return RemoteBrokerIp;
    }

    public void setRemoteBrokerIp(String remoteBrokerIp) {
        RemoteBrokerIp = remoteBrokerIp;
    }

    public int getRemoteBrokerPort() {
        return RemoteBrokerPort;
    }

    public void setRemoteBrokerPort(int remoteBrokerPort) {
        RemoteBrokerPort = remoteBrokerPort;
    }

    public void setPort(int port){
        this.port=port;
    }

}
