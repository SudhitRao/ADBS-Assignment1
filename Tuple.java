package ed.inf.adbs.lightdb;

import java.util.ArrayList;



public class Tuple {
    private ArrayList<Integer> fields;
    private int len;

    public Tuple(String[] param) {
        this.len = param.length;
        fields = new ArrayList<>();

        for (String s : param) {
            fields.add(Integer.parseInt(s));
        }
    }

    public Tuple(ArrayList<Integer> param) {
        this.len = param.size();
        this.fields = param;
    }

    

    @Override
    public String toString() {
        return fields.toString();
    }

    public int get(int index) {
        return fields.get(index);
    }

    public int getLength() {
        return len;
    }
}