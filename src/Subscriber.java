import DataTypes.Topic;
import DataTypes.Value;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Subscriber extends Node implements Serializable {
    private static final long serialVersionUID = -2122691439868668146L;
    private String brokerIp = "192.168.1.65";
    private int brokerport= 4202;
    Socket socket =new Socket();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public Subscriber(){

    }
    public Subscriber(String ip, int port){
        super(port,ip);
    }

    public void findBroker(Topic topic){
        for(Brocker b:BrokerList ){
            for(int i=0;i<b.brokerRangeList.size();i++) {
                if (topic.getBusLine().equals(b.brokerRangeList.get(i)[1])) {
                    brokerIp=b.getIpAddress();
                    brokerport=b.getPort();
                }
            }
        }
    }

    public void init(){
        try {
            socket = connect(brokerIp,brokerport);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end void

    public void EstablishConnection() {
        try {
            System.out.println(in.readUTF());
            out.writeUTF("BrokerList");
            out.flush();
            BrokerList=(ArrayList<Brocker>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**register for the first time for a topic**/
    public void register(Topic topic){
        try {
            init();
            System.out.println(in.readUTF());
            out.writeUTF("Subscribe");
            out.flush();
            out.writeObject(topic);
            out.flush();
            Value localValue = (Value) in.readObject();
            System.out.println("sub: "+ localValue.getBus().getBusLineId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            Disconnect(socket);
        }
    }//end register
    /**disconnect from the topic does not receive any more data **/
    private void disconnect(Topic topic){
        try {
            System.out.println(in.readUTF());
            out.writeUTF("Unsubscribe");
            out.flush();
            out.writeObject(topic);
            out.flush();
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Disconnect(socket);
        }
    }//end disconnect
    /**print the data in a readable format**/
    private void VisualiseData(){}
    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public int getBrokerport() {
        return brokerport;
    }

    public void setBrokerport(int brokerport) {
        this.brokerport = brokerport;
    }
}//end class Subscriber
