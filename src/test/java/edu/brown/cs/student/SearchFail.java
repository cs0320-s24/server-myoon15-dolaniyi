package edu.brown.cs.student;

public record SearchFail(String response_type) {
    public SearchFail(){
        this("error");
    }
}
