package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

public class HighScoreSystem {
    private static String names[];
    private static int results[];
    static{
        names = new String[10];
        results = new int[10];
        Writer writer = null;
        if (!Gdx.files.local("score.dat").exists()) {
            try {
                writer = Gdx.files.local("score.dat").writer(false);
                for (int i = 0; i < 10; i++) {
                    writer.write("Player 0\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void initTopScoreTab(){
        BufferedReader bReader = null;
        try {
            bReader = Gdx.files.local("score.dat").reader(8192);
            for (int i = 0; i < 10; i++){
                String str[] = bReader.readLine().split(" ");
                if (str[0].equals(null)&&Integer.parseInt(str[1]) == 0){
                    names[i] = "Player_Name";
                    continue;
                }
                names[i] = str[0];
                results[i] = Integer.parseInt(str[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveResult(String name, int score){
        int position;
        Writer writer = null;
        try {
            for (position = 0; position < results.length ; position++) {
                if (results[position]<score)break;
            }
            for (int i = 9; i > position; i--){
                results[i] = results[i-1];
                names[i] = names[i-1];
            }
            results[position] = score;
            names[position] = name;
            writer = Gdx.files.local("score.dat").writer(false);
            for (int i = 0; i < 10; i++) {
                writer.write(names[i] + " " + results[i] + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String showTable(){
        String str = names[0]+" "+results[0]+"\n";
        for (int i = 1; i < 10; i++) {
            str += names[i]+" "+results[i]+"\n";
        }
        return str;
    }

    public static boolean isResultFit(int score){
       return results[9]<score;
    }

}
