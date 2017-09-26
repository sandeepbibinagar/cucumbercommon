package com.experian.automation.config;

import org.apache.commons.configuration2.convert.DefaultConversionHandler;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;

import java.util.ArrayList;

public class CustomConversionHandler extends DefaultConversionHandler {

    public <T> T to(Object src, Class<T> targetCls, ConfigurationInterpolator ci) {

        if ( src.getClass().equals(ArrayList.class)){
            ArrayList list = (ArrayList) src;
            //src = list.get(list.size() - 1);
            src = list.get(0);
        }

        return super.to(src, targetCls, ci);
    }

}
