package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.MessageData;

public class SessionData extends MessageData{

    public String discoveryURL;
    public String contextURL;
    public String directoryURL;

    public String contextSessionKey;
    public String discoverySessionKey;
    public String directorySessionKey;
}
