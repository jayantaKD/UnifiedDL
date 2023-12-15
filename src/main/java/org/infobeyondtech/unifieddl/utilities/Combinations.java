package org.infobeyondtech.unifieddl.utilities;

import org.infobeyondtech.unifieddl.processingzone.VertexPair;

import java.util.ArrayList;

/******************************************************************************
 *  Compilation:  javac Combinations.java
 *  Execution:    java Combinations n
 *
 *  Enumerates all subsets of n elements using recursion.
 *  Uses some String library functions.
 *
 *  Both functions (comb1 and comb2) print them in alphabetical
 *  order; comb2 does not include the empty subset.
 *
 *  % java Combinations 3
 *
 *  a
 *  ab
 *  abc
 *  ac
 *  b
 *  bc
 *  c
 *
 *  a
 *  ab
 *  abc
 *  ac
 *  b
 *  bc
 *  c
 *
 *  Remark: this is, perhaps, easier by counting from 0 to 2^N - 1 by 1
 *  and looking at the bit representation of the counter. However, this
 *  recursive approach generalizes easily, e.g., if you want to print
 *  out all combinations of size k.
 *
 ******************************************************************************/

public class Combinations {
    // print all subsets of the characters in s
    public static void comb1(String s) { comb1("", s); }

    // print all subsets of the remaining elements, with given prefix
    private static void comb1(String prefix, String s) {
        if (s.length() > 0) {
            System.out.println(prefix + s.charAt(0));
            comb1(prefix + s.charAt(0), s.substring(1));
            comb1(prefix,               s.substring(1));
        }
    }

    // alternate implementation
    public static void comb2(String s) { comb2("", s); }
    private static void comb2(String prefix, String s) {
        System.out.println(prefix);
        for (int i = 0; i < s.length(); i++)
            comb2(prefix + s.charAt(i), s.substring(i + 1));
    }

    // alternate implementation
    public static ArrayList<ArrayList<VertexPair>> combVertexPairs(ArrayList<VertexPair> pairs) {
        ArrayList<ArrayList<VertexPair>> resultList = new ArrayList<ArrayList<VertexPair>> ();
        combVertexPairs(new ArrayList<VertexPair>(), pairs, resultList);
        return resultList;
    }

    private static void combVertexPairs(ArrayList<VertexPair> prefix, ArrayList<VertexPair> s, ArrayList<ArrayList<VertexPair>> results) {
        if(!prefix.isEmpty()){
//            results.add(prefix);
            System.out.println(prefix);
        }

        for (int i = 0; i < s.size(); i++) {
            ArrayList<VertexPair> tempPair = new ArrayList<VertexPair>();
            ArrayList<VertexPair> tempPrefix = new ArrayList<VertexPair>();
            for (int k=i+1; k < s.size(); k++){
                tempPair.add(s.get(k));
            }
            tempPrefix.addAll(prefix);
            tempPrefix.add(s.get(i));
            combVertexPairs(tempPrefix, tempPair, results);
        }
    }

//    private static void combVertexPairs(ArrayList<VertexPair> prefix, ArrayList<VertexPair> s) {
//        System.out.println(prefix);
//        for (int i = 0; i < s.size(); i++) {
//            ArrayList<VertexPair> tempPair = new ArrayList<VertexPair>();
//            ArrayList<VertexPair> tempPrefix = new ArrayList<VertexPair>();
//            for (int k=i+1; k < s.size(); k++){
//                tempPair.add(s.get(k));
//            }
//            tempPrefix.addAll(prefix);
//            tempPrefix.add(s.get(i));
//            combVertexPairs(tempPrefix, tempPair);
//        }
//    }

    // alternate implementation
    public static ArrayList<ArrayList<String>> combStrings(ArrayList<String> pairs) {
        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>> ();
        combStrings(new ArrayList<String>(), pairs,0);
        return resultList;
    }
    private static void combStrings(ArrayList<String> prefix, ArrayList<String> s, int startIndex) {
//        System.out.println(prefix);
        if(!prefix.isEmpty()){
            System.out.println(prefix);
           // results.add(prefix);
//            System.out.println();
        }

        //System.out.println();
        for (int i = startIndex; i < s.size(); i++) {
//            ArrayList<String> tempPair = new ArrayList<String>();
            ArrayList<String> tempPrefix = new ArrayList<String>();
//            for (int k=i+1; k < s.size(); k++){
//                tempPair.add(s.get(k));
//            }
            tempPrefix.addAll(prefix);
            tempPrefix.add(s.get(i));
//            System.out.print(s.get(i));
            combStrings(tempPrefix, s, i+1);
        }
    }

    public static ArrayList<ArrayList<Integer>> combIntegers(Integer[] pairs) {
        ArrayList<ArrayList<Integer>> resultList = new ArrayList<ArrayList<Integer>> ();
        combIntegers(0, pairs,0);
        return resultList;
    }
    private static void combIntegers(long sum, Integer[] s, int startIndex) {
//        System.out.println(prefix);
        if(sum > 0){
//            System.out.println(sum);
            // results.add(prefix);
//            System.out.println();
        }

        //System.out.println();
        for (int i = startIndex; i < s.length; i++) {
//            ArrayList<String> tempPair = new ArrayList<String>();
//            ArrayList<Integer> tempPrefix = new ArrayList<Integer>();
//            for (int k=i+1; k < s.size(); k++){
//                tempPair.add(s.get(k));
//            }
//            tempPrefix.addAll(prefix);
//            tempPrefix.add(s.get(i));
//            System.out.print(s.get(i));

            combIntegers(1, s, i+1);
        }
    }


    // read in N from command line, and print all subsets among N elements
    public static void main(String[] args) {
        int n = 5;
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String elements = alphabet.substring(0, n);

        System.out.println(elements);

        // using first implementation
//        comb1(elements);
//        System.out.println();

        // using second implementation
//        comb2(elements);
//        System.out.println();

        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");

        ArrayList<ArrayList<String>> results = combStrings(list);
        System.out.println(results);
    }
}
