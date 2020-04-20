// A Java program for a Server 
import java.net.*; 
import java.io.*; 

public class Server 
{ 
	
	static volatile int totalConn=0;

	public static void main(String args[]) throws IOException {  

 

		ServerSocket ss=new ServerSocket(5000);
		
		Socket collSock[]= new Socket[10];
		 

		  while(true)
		  {		
		   try{	
		 			
			 
					Socket s=ss.accept(); 
					collSock[totalConn++]= s; 

					System.out.println(totalConn);
					
					DataInputStream din=new DataInputStream(s.getInputStream());  
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
					  
					
					new Thread(new Runnable(){
						@Override
						public void run() {
			
			     
							String str=""; 
					
					while(true){
			
					try{
					str=din.readUTF();
					
					for(int i=0;i<new Server().totalConn;i++)
					 if(s!=collSock[i])
					 {
					  DataOutputStream dout=new DataOutputStream(collSock[i].getOutputStream());  
					  dout.writeUTF(str);
					}
					
					if(str.equals(null))
					{s.close();
					break;}
					
					System.out.println("client says: "+str+ new Server().totalConn);  
					}catch(Exception e){

						for(int i=0;i<new Server().totalConn;i++)
						 if(s== collSock[i])
						 {
							  collSock[i]= collSock[totalConn-1];
							  totalConn--;
						 }
						 
						break;
					}
				
					
					
					}  
			
						}
					}).start();
					
			
					
					// new Thread(new Runnable(){
					
					// 	@Override
					// 	public void run() {
							
					// 		while(true) 
					// 		{
					// 			try{
					// 			dout.writeUTF("str");
					// 			Thread.sleep(5000);}
					// 			catch(Exception e)
					// 			{
					// 				System.out.println(e.getMessage());
					// 			}
					// 		}
			
					// 	}
					// }).start();
			
				
			
				}
				 catch(Exception e)
				 { 
					 System.out.println(e.getMessage());
				 }

				}

			}
		}
