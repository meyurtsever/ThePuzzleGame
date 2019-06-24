/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thepuzzlegame;

/**
 *
 * @author Yurtsever
 */
public class Player {
       
    private int moves;
    private String userName;
    private double score;  
    
    public Player() {
         this.moves = 0;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" + "moves=" + moves + ", userName=" + userName + ", score=" + score + '}';
    }  
 
}
