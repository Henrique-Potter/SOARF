/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.exceptions;

/**
 *
 * @author henrique
 */
public class AgentConfigConsistencyException extends ServiceBaseException{
        private SimpleExceptionBean faultBean;


    /**
     *
     * @param message
     */
    public AgentConfigConsistencyException(String message) {
        super(message);
    }

    public AgentConfigConsistencyException(String message, SimpleExceptionBean faultInfo) {
        super(message);
        faultBean = faultInfo;
    }

    public AgentConfigConsistencyException(String message, SimpleExceptionBean faultInfo, Throwable cause) {
        super(message, cause);
        faultBean = faultInfo;
    }

    public SimpleExceptionBean getFaultInfo() {
        return faultBean;
    }

    /**
     *
     * @param e
     */
    public AgentConfigConsistencyException(Exception e) {
        super(e);
    }
}
