/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.agent.excpetions;

/**
 *
 * @author henrique
 */
public class InvalidByteArrayFormat extends Exception{

    public InvalidByteArrayFormat(String message) {
        super(message);
    }

    public InvalidByteArrayFormat(String message, Throwable cause) {
        super(message, cause);
    }

    
    
}
