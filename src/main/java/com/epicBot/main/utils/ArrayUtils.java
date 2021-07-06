package com.epicBot.main.utils;

import com.epicBot.main.messageProcessing.commands.quote.Quote;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    public static <E> List<E> reverse(List<E> l){
        List<E> ogList = new ArrayList<>(l);
        l.clear();
        for (int i = ogList.size() - 1; i >= 0; i--) {
            l.add(ogList.get(i));
        }
        return l;
    }

}
