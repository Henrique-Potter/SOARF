package br.edu.ocdrf.query;

import java.util.ArrayList;

public class DirectoryQueryType1 {

    private boolean includeFederation;
    private DirectoryQueryType1Target target;
    private ArrayList<DirectoryQueryType1Target> targetList = new ArrayList<>();

    public DirectoryQueryType1Target getTarget() {
        return target;
    }

    public void setTarget(DirectoryQueryType1Target target) {
        this.target = target;
    }

    public boolean isIncludeFederation() {
        return includeFederation;
    }

    public void setIncludeFederation(boolean includeFederation) {
        this.includeFederation = includeFederation;
    }

    public ArrayList<DirectoryQueryType1Target> getTargetList() {
	return targetList;
    }

    public void setTargetList(ArrayList<DirectoryQueryType1Target> targetList) {
	this.targetList = targetList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (includeFederation ? 1231 : 1237);
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DirectoryQueryType1 other = (DirectoryQueryType1) obj;
        if (includeFederation != other.includeFederation) {
            return false;
        }
        if (target == null) {
            if (other.target != null) {
                return false;
            }
        }
        else if (!target.equals(other.target)) {
            return false;
        }
        return true;
    }
}
