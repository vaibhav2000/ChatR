import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
 
    static Vector<Socket> socketHolder= new Vector<>();

    public static void main(String[] args) throws Exception
    {   
        ServerSocket srvsock= new ServerSocket(5000);
        System.out.println("Console LOG:");

        while(true)
        {

            try{

                Socket s= srvsock.accept();

                socketHolder.add(s);
                System.out.println(s.toString()+ " added, Count="+socketHolder.size());


                DataInputStream inp = new DataInputStream(s.getInputStream()); 

                new Thread(new Runnable(){
                
                    @Override
                    public void run() {
                        

                    while(true){ 
                        try{ 
                        String str= inp.readUTF(); 




                        System.out.println(s.toString()+" says "+str);

                        
                        for(int i=0;i<socketHolder.size();i++) 
                         if(socketHolder.get(i) != s)
                          {
                               DataOutputStream ds= new DataOutputStream(socketHolder.get(i).getOutputStream());
                               ds.writeUTF(str);
                          }
                        
                        }catch(Exception e)
                        {
                             socketHolder.remove(s);
                             System.out.println(s.toString()+" left,Count="+socketHolder.size()); 
                            
                             try{s.close();}
                             catch(Exception ex){};
                             return;

                        }

                        }  
                    }
                }).start();
                

            }
            catch(Exception e){e.printStackTrace();}           
        }
    }
   
}