import java.io.*;
import java.net.*;
import java.util.*;

public class Client {


    public static void main(String arg[]) throws Exception
    {

       try{ 
      Socket s=new Socket("192.168.43.45",5000);
      DataOutputStream outp=  new DataOutputStream(s.getOutputStream());
      DataInputStream inp = new DataInputStream(s.getInputStream()); 

     
      new Thread(new Runnable(){
      
        @Override
        public void run() {
            // TODO Auto-generated method stub
            

            while(true)
            {
              
                java.util.Scanner sc = new Scanner(System.in);
                String str= sc.nextLine();

                try{
                outp.writeUTF(str); 
                }catch(Exception e){}
            }
            
        }
    }).start();


    new Thread(new Runnable(){
      
        @Override
        public void run() {

            while(true)
            {
              try{
               String str=  inp.readUTF();
               System.out.println(str);
              }catch(Exception e){}
            }
            
        }
    }).start();


     
       } catch(Exception e){e.printStackTrace();}
    

     
}
}