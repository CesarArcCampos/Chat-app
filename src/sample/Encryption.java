package sample;

import java.util.ArrayList;
import java.util.Collections;

public class Encryption {

    private final ArrayList<Character> list;
    private ArrayList<Character> shuffledList;
    private char character;
    private char[] letters;

    public Encryption() {
        list = new ArrayList<>();
        shuffledList = new ArrayList<>();
        character = ' ';
        newKey();
    }

    public void newKey() {
        character = ' ';
        list.clear();
        shuffledList.clear();

        for (int i=32; i< 127; i++) {
            list.add(character);
            character++;
        }

        shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList);
        System.out.println("> Generated Encryption Key.");
    }

    public String encrypt(String message) {
        letters = message.toCharArray();

        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (letters[i] == list.get(j)) {
                    letters[i] = shuffledList.get(j);
                    break;
                }
            }
        }
        return new String(letters);
    }

    public String decrypt(String message) {
        letters = message.toCharArray();

        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < shuffledList.size(); j++) {
                if (letters[i] == shuffledList.get(j)) {
                    letters[i] = list.get(j);
                    break;
                }
            }
        }
        return new String(letters);
    }

    public ArrayList<Character> getKey() {
        return shuffledList;
    }
}
