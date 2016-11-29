package edu.tufts.cs.twocents;

import static android.R.attr.id;

/**
 * Created by toby on 10/27/16.
 * ApiMethods
 */

public enum ApiMethods {
    CREATE_NEW_USER("Create New User"), IS_USERNAME_AVAILABLE("Is Username Available"),
    CREATE_NEW_POLL("Create New Poll"), VOTE_ON_POLL("Vote on Poll"), GET_POLLS("Get Polls"),
    GET_POLLS_FOR_USER("Get Polls for User");

    private final String name;
    ApiMethods(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
