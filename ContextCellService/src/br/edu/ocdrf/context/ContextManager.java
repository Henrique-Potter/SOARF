package br.edu.ocdrf.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import br.edu.ocdrf.entities.ResourceState;

/**
 * @author andre
 *
 */
public class ContextManager implements Serializable {

    private static final long serialVersionUID = 1762498889034406057L;

    private final Map<String, Stack<ResourceState>> states = new HashMap<>();
    private int capacity = 30;

    /**
     *
     * @param uri
     * @param state
     */
    public synchronized void put(String uri, ResourceState state) {
        Stack<ResourceState> statesOfResource;
        if (!states.containsKey(uri)) {
            statesOfResource = new Stack<>();
            statesOfResource.setSize(capacity);
            states.put(uri, statesOfResource);
        }
        else {
            statesOfResource = states.get(uri);
        }
        if (statesOfResource.size() == capacity) {
            statesOfResource.remove(0);
        }
        statesOfResource.push(state);
    }

    /**
     *
     * @param resourceURI
     * @return
     */
    public synchronized boolean contains(String resourceURI) {
        return states.containsKey(resourceURI);
    }

    /**
     *
     * @param resourceURI
     * @return
     */
    public synchronized ResourceState get(String resourceURI) {
        Stack<ResourceState> statesOfResource = states.get(resourceURI);
        return statesOfResource.peek();
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
