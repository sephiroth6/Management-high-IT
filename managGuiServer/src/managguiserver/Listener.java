/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managguiserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Claudio
 */
public class Listener implements Runnable {
    
    private ServerSocket so;
    private Server.ServerSingleton ss;
    
    public Listener(ServerSocket a, Server.ServerSingleton b) {
        this.so = a;
        this.ss = b;
    }
    
    @Override
    public void run () {
        
        try {
            
            while (true)
                new Server.ServerRunnable(this.so.accept(), this.ss).run();
            
        } catch (IOException e) {
            
        }
        
    }
    
}
