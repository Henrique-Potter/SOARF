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
public class ServiceBaseException extends Exception {

    public ServiceBaseException() {
    }

    public ServiceBaseException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     *
     * @param message
     */
    public ServiceBaseException(String message) {
        super(message);
    }

    /**
     *
     * @param e
     */
    public ServiceBaseException(Exception e) {
        super(e);
    }

}
