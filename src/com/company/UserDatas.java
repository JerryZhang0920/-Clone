package com.company;

import java.io.*;
import java.lang.*;
import java.util.*;

// class UserDatas
// Purpose: sort user's maxtiles and moves
// represent a user
public class UserDatas{
    //https://www.geeksforgeeks.org/collections-sort-java-examples/
    String name;
    int maxTile;
    int moves;

    // constructor
    public UserDatas(String name, int maxTile, int moves) {
        this.name = name;
        this.maxTile = maxTile;
        this.moves = moves;
    }

    // use to print user details in main
    public String toString(){
        return this.name+ " "+ this.maxTile+ " "+ this.moves;
    }

    // main method
   public static void main(String[] args){
        // initialize arrayList of userDatas
        ArrayList<UserDatas> userRank = new ArrayList<>();
        // use for testing
        for(int i = 0; i<userRank.size(); i++){
            System.out.println(userRank.get(i));
        }
       Collections.sort(userRank, new MoveComparator());
       Collections.sort(userRank, new MaxTileComparator());
        // use for testing
       for(int i = 0; i<userRank.size(); i++){
           System.out.println(userRank.get(i));
       }

   }
}
// Class: MaxTileComparator
// Return: userRank sorted in descending order based on maxTile
class MaxTileComparator implements Comparator<UserDatas> {
    @Override
    public int compare(UserDatas a, UserDatas b){
        //https://stackoverflow.com/questions/41141672/sorting-in-descending-order-using-comparator
        // descending order
        // return a.maxTile > b.maxTile ? -1 :(a.maxTile < b.maxTile ? 1 : 0);
        return b.maxTile - a.maxTile;
    }
}
// Class: MoveComparator
// Return: userRank sorted in ascending order based on moves
class MoveComparator implements Comparator<UserDatas> {
    @Override
    public int compare(UserDatas a, UserDatas b){
        // sort in ascending order
        return a.moves - b.moves;
    }
}

