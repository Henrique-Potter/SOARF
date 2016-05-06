package br.edu.ocdrf.query;

public class DirectoryQueryType2 {

    private DirectoryQueryType2Target target;

    public DirectoryQueryType2Target getTarget() {
        return target;
    }

    public void setTarget(DirectoryQueryType2Target target) {
        this.target = target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final DirectoryQueryType2 other = (DirectoryQueryType2) obj;
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
