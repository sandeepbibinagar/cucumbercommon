package com.experian.common.helpers;

import org.testng.internal.collections.Pair;

import java.io.*;
import java.util.Properties;
import java.util.Stack;

public class PropertiesConfigurator{
    private File configFile;
    private Reader r;
    private Writer w;
    private Properties props;
    private Stack<Pair<String, String>> stack;

    public PropertiesConfigurator(final String filePath) throws IOException {
        configFile = new File(filePath);
        prepare();
        stack = new Stack<Pair<String, String>>();
    }

    public void setProperties(String key, String value) {
        stack.push(Pair.create(key, readProperties(key)));
        props.setProperty(key, value);
    }

    public String readProperties(String key) {
        return props.getProperty(key);
    }

    public void save() throws IOException {
        props.store(w, "setting the properties");
        w.close();
    }

    public void revert() throws IOException {
        prepare();

        while(!stack.empty()){
            Pair<String, String> p = stack.pop();
            props.setProperty(p.first(), p.second());
        }
        save();
    }

    private void prepare() throws IOException {
        r = new FileReader(configFile);
        props = new Properties();
        props.load(r);
        r.close();

        w = new FileWriter(configFile);
    }
}
