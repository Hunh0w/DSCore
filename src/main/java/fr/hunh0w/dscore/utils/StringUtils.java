package fr.hunh0w.dscore.utils;

import java.util.List;

public class StringUtils {

    public static String joinArray(List<String> array, String joinString){
        String result = "";
        for(int i = 0; i < array.size(); i++){
            if(i == 0) result = array.get(0);
            else result = result + joinString + array.get(i);
        }
        return result;
    }

}
