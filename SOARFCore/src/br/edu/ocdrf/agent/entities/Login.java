package br.edu.ocdrf.agent.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Login {

    @XStreamAlias("UserID")
    @XStreamAsAttribute
    public String userid;
    
    @XStreamAlias("Password")
    @XStreamAsAttribute
    public String password;

}
