package edu.brown.cs.student;

import java.util.Map;

public record SearchSuccess(String response_type, Map<String, Object> responseMap) {
    public SearchSuccess(Map<String, Object> responseMap){
        this("success", responseMap);
    }

}
