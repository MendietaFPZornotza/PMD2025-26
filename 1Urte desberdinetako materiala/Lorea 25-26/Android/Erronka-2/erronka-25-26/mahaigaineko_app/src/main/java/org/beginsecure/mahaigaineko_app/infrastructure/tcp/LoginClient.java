package org.beginsecure.mahaigaineko_app.infrastructure.tcp;

public class LoginClient extends TcpClient {

    /**
     * Saioa hasteko eskaera bidaltzen du.
     */
    public static String login(String email, String password) throws Exception {
        String message = String.join(";", "LOGIN", email, password);
        return sendMessage(message);
    }
}