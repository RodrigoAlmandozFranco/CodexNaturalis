package it.polimi.ingsw.am42.controller;

/**
 *
 * Enumeration with all the possible ways a player can connect to server
 *
 * @author Tommaso Crippa
 */
public enum ConnectionState {
    // No one connected yet, no saved game
    CREATE,
    // Connecting to brand new game already
    CONNECT,
    // No one connected yet, game saved in memory
    LOAD,
    // Connecting to saved game
    LOADING
}
