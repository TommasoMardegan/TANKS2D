import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

//per impostare IP e PORT del server -> in classe Messaggio.java
//classe principale del server che contiene il main
public class Server {
    //indice che indica la lettera del carro corrente (es: A = indice 0)
    public static int indiceLettera = 0;
    //gestisco 2 client
    static String[] lettere = {"A", "B"};
    //posizioni iniziali dei carri in x e y
    static int[] posizioneIniX = {600, 100};
    static int[] posizioneIniY = {550, 550};
    //costante che indica il numero di giocatori ammessi
    final static int NUMERO_GIOCATORI = 2;
    //porta del server
    final static int serverPort = 666;

    //MAIN del server
    public static void main(String[] args) throws IOException {
        //aggiungo i due carri alla gestione gioco
        Carro carro1 = new Carro("images/A_tank_up.png", "A", posizioneIniX[0], posizioneIniY[0]);
        Carro carro2 = new Carro("images/B_tank_up.png", "B", posizioneIniX[1], posizioneIniY[1]);

        //creo l'oggetto per gestire e controllare i movimenti, gli spari, le collisioni etc. dal lato server
        GestioneGioco gc = new GestioneGioco();
        //ci aggiungo i carri
        gc.addClientCarro(carro1);
        gc.addClientCarro(carro2);

        //creo la socket che ascolterà le richieste client
        ServerSocket serverSocket = new ServerSocket(serverPort);

        //gestisco la creazione dei thread client (uno per client)
        while (true) {
            //collegamento di un nuovo client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nuovo client connesso");
            //controllo che i giocatori connessi non siano più di quelli voluti
            if(indiceLettera < NUMERO_GIOCATORI) {
                //creo il thread che gestisce il singolo client (carro)
                Thread clientThread = new Thread(new ThreadClient(clientSocket, gc, indiceLettera,lettere, posizioneIniX, posizioneIniY));
                //avvio il thread
                clientThread.start();
                //aumento l'indice lettera (il primo carro sarà A, indice 0; il secondo sarà B; indice 1)
                indiceLettera++;
            }
        }
    }
}