/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Common.KMessage;
import java.io.IOException;

/**
 *
 * @author Nguyen Cao Ky
 */
public interface inReceiveMessage {
    
    public void ReceiveMessage(KMessage msg) throws IOException;
}
