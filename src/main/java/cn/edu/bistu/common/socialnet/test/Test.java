package cn.edu.bistu.common.socialnet.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanjie on 11/15/15.
 */
public class Test {
    private static String filePath = "/Users/tanjie/Documents/Master Files/Corpus/Data";

    public static void main(String[] args) throws IOException {
        String[] name = new String[15];
        int[] v = new int[15];
        String[] mname = new String[15];
        int[] mv = new int[15];
        for(int i =0;i<15;i++){
            name[i]="";
            v[i]=0;
            mname[i]="";
            mv[i]=Integer.MAX_VALUE;
        }
        List<String> all = new ArrayList<>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for(File f: files) {
            all.add(f.getName());
        }

        for(String s: all) {
            String path = filePath + "/" + s + "/weibo.txt";
            if(!new File(path).exists())
                continue;
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            String line = br.readLine();
            String[] ls = line.split(" +");
            String n = ls[0];
            int va = Integer.parseInt(ls[5]);
            int index = getMin(v);
            if(v[index] < va) {
                name[index] = n;
                v[index] = va;
            }
            index = getMax(mv);
            if(mv[index]>va) {
                mname[index] = n;
                mv[index] = va;
            }
        }

        for(int i =0;i<15;i++){
            System.out.println(name[i] + "\t" + v[i]);
        }
        System.out.println();
        for(int i =0;i<15;i++){
            System.out.println(mname[i] + "\t" + mv[i]);
        }
    }

    public static int getMin(int[] a) {
        int in = 0;
        int min = a[0];
        for(int i=1;i<a.length;i++){
            if(a[i]<min){
                in = i;
                min=a[i];
            }
        }
        return in;
    }
    public static int getMax(int[] a) {
        int in = 0;
        int max = a[0];
        for(int i=1;i<a.length;i++){
            if(a[i]>max){
                in = i;
                max=a[i];
            }
        }
        return in;
    }
}
