package br.edu.ocdrf.interfaces;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author andre
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IContextObserver {

    /**
     *
     * @param state
     */
    void resourceState(String state);

}
