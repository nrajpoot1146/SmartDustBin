package smartdust;
import java.net.*;
import java.io.*;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 

public class Sserver extends Thread{
	private ServerSocket serverSocket;
	private JavaEmail javaEmail;
	private boolean serverFlag;
	private int port;
	
	@Override
	public void run() {
		while(serverFlag) {
			try {
				smartbin.appui.append("Waiting for client on port"+ InetAddress.getLocalHost()+":" + serverSocket.getLocalPort() + "...");
				Socket client = serverSocket.accept();
				smartbin.appui.append("Client connected....");
				new communication(client).start();;
			}catch(SocketTimeoutException s) {
				System.out.println(s.getMessage());
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	@Override
	public void start(){
		this.serverFlag = true;
		try {
			if(serverSocket==null) {
				serverSocket = new ServerSocket(port);
				smartbin.appui.append("Server started....");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.start();
	}
	
	public void destroy() {
		this.serverFlag = false;
		try {
			if(serverSocket!=null) {
				serverSocket.close();
				serverSocket = null;
				smartbin.appui.append("Server stoped....");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Sserver(int port) {
	      serverFlag = false;
	      this.port = port;
	      //serverSocket.setSoTimeout(10000);
	}
	
	class communication extends Thread{
		private Socket s;
		@Override
		public void run() {
			try {
				BufferedReader di = new BufferedReader(new InputStreamReader(s.getInputStream()));
				BufferedWriter br = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				
				String str = di.readLine();
				System.out.println(str);
				if(str.startsWith("GET")) {
					PrintWriter out = new PrintWriter(s.getOutputStream());
					if(str.indexOf("/ ")!=-1) {
						out.println("HTTP/1.1 200 OK");
			            out.println("Content-type: text/html");
			            out.println("\r\n");
			            
			            FileInputStream fin = new FileInputStream("D:\\web_pro\\pranjal\\index.html");
			            int i;
			            String res= null;
			            while((i = fin.read())!=-1) {
			            	res += (char)i;
			            }
			            out.println(res);
					}else if(str.indexOf("?")==-1) {
						if(str=="favicon.ico") {
							return;
						}
						String tstr = str.substring(str.indexOf("/")+1,str.indexOf("H")-1);
						FileInputStream fin = new FileInputStream("D:\\web_pro\\pranjal\\"+tstr);
			            int i;
			            //fin.read(b)
			            String res= null;
			            while((i = fin.read())!=-1) {
			            	res += (char)i;
			            }
			            out.println(res);
						
					}
					 
					
					//br.flush();
		            out.flush();
				}else {
					smartbin.appui.append(str);
					if(str.startsWith("{")){
						takeAction(str);
					}
				}
				
				String st = "ok#";
				br.write(st,0,st.length());
				br.flush();
				di.close();
				br.close();
				s.close();
				System.out.println("close");
				
			}catch(SocketTimeoutException s) {
				System.out.println(s.getMessage());
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}catch(Exception e) {
				
			}
		}
		public communication(Socket s) {
			this.s=s;
		}
		private void takeAction(String str) throws Exception{
			JSONObject jo = null;
			jo = (JSONObject) new JSONParser().parse(str);
			
			String binId = (String) jo.get("binId");
			String binStatus = (String) jo.get("binStatus");
			
			javaEmail = new JavaEmail();
			javaEmail.sendMail(binStatus, binId);
			smartbin.db.saveStatus(str);
		}
	}
}
