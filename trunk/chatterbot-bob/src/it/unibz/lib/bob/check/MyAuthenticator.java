package it.unibz.lib.bob.check;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
//	 This method is called when a password-protected URL is accessed
    protected PasswordAuthentication getPasswordAuthentication() {
       /*
		 * // Get information about the request String promptString =
		 * getRequestingPrompt(); String hostname = getRequestingHost();
		 * InetAddress ipaddr = getRequestingSite(); int port =
		 * getRequestingPort();
		 */
        // Get the username from the user...
        String username = "mkirschner";
        //String username = "bob-webapp";

        // Get the password from the user...
        String password = "";
        //String password = "sNK212lrt";

        // Return the information
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
