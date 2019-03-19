import DataTypes.Topic;
import DataTypes.Value;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Publisher extends Node{
    private String myHash=null;
    private Node node = null;
    Md5 md5= new Md5();

    /**hashes the DataTypes.Topic it recives and compairs it with the
     * hashes that exist in the BrokerList **/
    public Brocker hashTopic(Topic topic){
        Md5 md5 = new Md5();
        myHash=md5.HASH(topic.getBusLine());
        for (Brocker brocker : getBrokerList()){
            if(myHash.compareTo(brocker.getBrokerRange())==-1){
                return brocker;
            }
            else if(Integer.parseInt(myHash)%Integer.parseInt(brocker.getBrokerRange())< Integer.parseInt(brocker.getBrokerRange())){
                return brocker;
            }
        }
        System.out.println("Broker does not exists");
        return null;
    }

    public String getMyHash(){
        Md5 md5 =new Md5();
        myHash=md5.HASH("localhost"+Integer.toString(4202));
        return myHash;
    }

    /**this function gets the list of all Brokers via tcp connection **/
    public  ArrayList<Brocker> getBrokerList(String Ip ,int port){
        Socket socket=null;
        try {
            socket= new Socket(Inet4Address.getByName(Ip),port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeUTF("hi");
            in.read();
            return (ArrayList<Brocker>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void send(){
        Socket socket=connect("localhost",4201);
        try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String anser = in.readUTF();
                System.out.println(anser);
                out.writeUTF(getMyHash());
                out.flush();
                System.out.println(in.readUTF());
                in.close();
                out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Disconnect(socket);
        }
    }

    public void push (Value value ,Topic topic) throws IOException {
        Socket socket=super.connect("localhost",4201);
        ObjectOutputStream out = null;
       // socket= new Socket(InetAddress.getByName(ip),port);
        out= new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(topic);
        out.flush();
        out.close();
    }

    public void notitfyFailure(Brocker  broker){

    }
}
