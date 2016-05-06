package br.edu.ocdrf.exceptions;


/**
 * Gerenciador de exceções do CR-RIO.
 *
 * @author André Luiz Barbosa Rodrigues <albr74@gmail.com>
 * @since 24 / 10 / 2007
 * @version 1.0
 * @category Gerenciamento de Exceções
 *
 */
public class ExceptionManager {

    private static ExceptionManager singleton;

    static {
        singleton = new ExceptionManager();
    }

    /**
     *
     */
    protected ExceptionManager() {
        super();
    }

    /**
     *
     * @return
     */
    public static ExceptionManager getInstance() {
        return singleton;
    }

    /**
     *
     * @param ex
     */
    public void managerException(Exception ex) {
       
    }
}