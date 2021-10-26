package edu.tongji.aising.XMLRepository.util;

import edu.tongji.aising.XMLRepository.util.anno.Nullable;

public abstract class Assert {

    public static void notNull(@Nullable Object object, String message){
        if(object == null){
            throw new IllegalArgumentException(message);
        }
    }

}
