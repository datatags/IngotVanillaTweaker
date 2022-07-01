/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.budderman18.IngotVanillaTweaker.Data;

/**
 *
 * This enum defines types for tooexpensivefix
 * 
 */
public enum OutputType {
    //types
    /**
     *
     * Uses Chat for output
     * 
     */
    CHAT("chat"),
    /**
     *
     * Uses Action Bar for output
     * 
     */
    ACTION_BAR("action_bar"),
    /**
     *
     * Uses Title for output
     * 
     */
    TITLE("title");
    //global vars
    private String type;
    //constructor
    private OutputType(String string) {
        type = string;
    }
    /**
     * 
     * This method gets the given type from a string
     * 
     * @param string
     * @return 
     */
    public static OutputType getFromString(String string) {
        if (string.equalsIgnoreCase("chat")) {
            return CHAT;
        }
        else if (string.equalsIgnoreCase("action_bar")) {
            return ACTION_BAR;
        }
        else if (string.equalsIgnoreCase("title")) {
            return TITLE;
        }
        return null;
    }
}

