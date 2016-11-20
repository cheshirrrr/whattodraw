package whattodraw.suggestions;

import java.io.Serializable;

public class Suggestion implements Serializable{
    private String name;
    private int votes = 0;

    public Suggestion(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote(){
        votes++;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Suggestion)){
            return false;
        }
        Suggestion other = (Suggestion)obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
