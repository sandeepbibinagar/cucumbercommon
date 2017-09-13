package com.experian.common.config;

import org.apache.commons.configuration2.ConfigurationLookup;
import org.apache.commons.configuration2.ImmutableConfiguration;

import java.util.ArrayList;

public class CustomLookup extends ConfigurationLookup
{
    public CustomLookup(ImmutableConfiguration config) {
        super(config);
    }

    public Object lookup(String varName)
    {
        Object value = super.lookup(varName);

        if (value != null && value.getClass().equals(ArrayList.class)){
            ArrayList list = (ArrayList) value;
            //value = list.get(list.size() - 1);
            value = list.get(0);
        }

        return value;
    }
}