package alma.logoot.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alma.logoot.network.p2p.P2PLayer;
import alma.logoot.network.p2p.interfaces.OnReceiveHandler;

/**
 * Servlet implementation class GetData
 */
public class GetDataTwo extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static ArrayList<PrintWriter> clients = new ArrayList<PrintWriter>();
  private boolean master = false;

  private static P2PLayer p2p = P2PLayer.getInstance();
  
  // private static final int BUFFER_SIZE = 4000000;
  private static final int BUFFER_SIZE = 4096;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public GetDataTwo() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("doGet : session=> " + request.getSession(true).getId());
    response.setHeader("pragma", "no-cache,no-store");
    response.setHeader("cache-control",
        "no-cache,no-store,max-age=0,max-stale=0");
    response.setHeader("Accept-Charset", "utf-8");
    response.setContentType("text/event-stream;charset=utf-8;");
    response.setCharacterEncoding("UTF-8");

    clients.add(response.getWriter());

    if (!master) {
      p2p.setOnReceiveHandler(new OnReceiveHandler() {

        @Override
        public void execute(String message) {
          System.out.println("Receive from P2P network: " + message);
          
          String clientSentence = (message.replaceAll("\n", "<br>"));
          for (PrintWriter out : clients) {
            out.print("data: " + clientSentence + "\n\n");
            out.flush();
          }
        }
      });

      master = true;
      System.out.println("GetData : Creation de la socket sur le port "
          + NetworkServiceImpl.PORTSEND);
      ServerSocket welcomeSocket = new ServerSocket(NetworkServiceImplTwo.PORTSEND);

      while (true) {
        Socket connectionSocket = welcomeSocket.accept();
        System.out.println("GetData : Acceptation de la socket");
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
            connectionSocket.getInputStream()));
        char[] buffer = new char[BUFFER_SIZE];
        inFromClient.read(buffer);

        String clientSentence = (new String(buffer).trim().replaceAll("\n",
            "<br>"));
        for (PrintWriter out : clients) {
          out.print("data: " + clientSentence + "\n\n");
          out.flush();
        }
      }

    } else {
      while (true) {
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  public void destroy() {
    System.out.println("End servlet: " + getServletInfo());
    super.destroy();
  }
}
