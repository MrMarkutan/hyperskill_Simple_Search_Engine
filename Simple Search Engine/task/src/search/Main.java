package search;


import java.io.*;
import java.util.*;

public class Main {

    Scanner scanner;
    List<String>people;
    boolean menu = false;
    static String dataFromFile = null;
    Map<String, List<Integer>> map;

    public static void main(String[] args) {
        if(args.length > 0){
            for (int i = 0; i < args.length; i++) {
                if(args[i].equals("--data")){
                    dataFromFile = args[i+1];
                }
            }
        }
        new Main().start();

     }
    void start(){
        scanner = new Scanner(System.in);
        people = new LinkedList<>();

        enterData();
//        show();

        menu();
    }
    void enterData() {
        if(dataFromFile == (null)){
            System.out.println("Enter the number of people:");
            int numOfPeople = Integer.parseInt(scanner.nextLine());

            System.out.println("\nEnter all people:");

            for (int i = 0; i < numOfPeople; i++) {
                people.add(scanner.nextLine());
            }
        }else {
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFromFile))){
                String line;
                while ((line = reader.readLine()) != null){
                    people.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        map = new HashMap<>();
        for (int i = 0; i < people.size(); i++) {
            String[] person = people.get(i).split(" ");
            for (String s : person) {
                List<Integer> list = new LinkedList<>();
                if(map.containsKey(s)){
                    list.addAll(map.get(s));
                    list.add(i);
                    map.replace(s, list);
                }
                else{
                    list.add(i);
                    map.put(s, list);
                }
            }
        }
    }
    void show(){
        System.out.println();
        for (int i = 0; i < people.size(); i++) {
            System.out.println(i + " " + people.get(i));
        }

        System.out.println();


        for (Map.Entry<String,List<Integer>> entry : map.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    void menu(){
        while (!menu){
        System.out.println("\n=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");

            switch (Integer.parseInt(scanner.nextLine())){
                case 1: search();break;
                case 2: printAll();break;
                case 0:
                    System.out.println("\nBye!");
                    menu = true;
                    break;
                default:
                    System.out.println("\nIncorrect option! Try again.");
                    break;
            }
        }
    }
    void search() {

        boolean all = false, none = false;
        StringBuilder stringBuilder = new StringBuilder();
        int numOfFound = 0;

        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
        switch (scanner.nextLine().toUpperCase()){
            case "ANY": break;
            case "ALL": all =true;
                break;
            case "NONE": none = true;
                break;
        }

        System.out.println("\nEnter a name or email to search all suitable people.");
        String query = scanner.nextLine().toLowerCase();
        String []splittedQuerry = query.split(" ");

        if(all){

            List<Integer> tmpList;
            StringBuilder tmpString = new StringBuilder();
outer:            for (String s : splittedQuerry){
                for (Map.Entry<String, List<Integer>> entry : map.entrySet()){

                    if(entry.getKey().toLowerCase().equals(s)){
                        tmpList = entry.getValue();
                        if(tmpList.size() > 1){
                            for (Integer inTmpList : tmpList) {
                                  if(query.contains(people.get(inTmpList))){
                                    tmpString.append(people.get(inTmpList)).append("\n");
                                    }
                                }
                            }
                        else if(tmpList.size() == 1){
                                    tmpString.append(people.get(tmpList.get(0))).append("\n");
                                    break outer;
                        }
                    }
                }
            }
            System.out.println(tmpString.toString());
        }
        else if(none){
           StringBuilder tmpString = new StringBuilder();
           for(Map.Entry<String, List<Integer>> entry : map.entrySet()){
                if(!query.contains(entry.getKey().toLowerCase())){
                    List<Integer> tmpList = entry.getValue();
                        for (Integer inTmpList: tmpList) {
                            boolean write = true;
                            for (String s : splittedQuerry) {
                                if (people.get(inTmpList).toLowerCase().contains(s)) {
                                    write = false;
                                    break;
                                }
                            }
                            if(write){
                                if (!tmpString.toString().contains(people.get(inTmpList))) {
                                    tmpString.append(people.get(inTmpList)).append("\n");
                                    numOfFound++;
                                    break;
                                }
                            }
                    }
                }
            }
            System.out.println(numOfFound == 0 ? "No matching people found.\n" : "\n" + numOfFound + " persons found:\n" + tmpString.toString());
        }
        else{
            for (String s: splittedQuerry) {
                for (Map.Entry<String, List<Integer>>entry : map.entrySet()) {
                    List<Integer> tmpList;

                    if(entry.getKey().toLowerCase().equals(s)) {
                        tmpList = entry.getValue();

                        for (int i = 0; i < tmpList.size(); i++) {
                            stringBuilder.append(people.get(tmpList.get(i))).append("\n");
                            numOfFound++;
                        }

                    }
                }
            }
        System.out.println(numOfFound == 0 ? "No matching people found.\n" : "\n" + numOfFound + " persons found:\n" + stringBuilder.toString());
        }
    }
    void printAll(){
        System.out.println("\n=== List of people ===");
        for (String person: people) {
            System.out.println(person);
        }
    }

}
