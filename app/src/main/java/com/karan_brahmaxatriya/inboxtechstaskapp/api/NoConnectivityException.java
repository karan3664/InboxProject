package com.karan_brahmaxatriya.inboxtechstaskapp.api;


import java.io.IOException;
/**
 * Created by Karan Brahmaxatriya on 02-Feb-21.
 */
public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "Internet Connectivity not found.";
    }
}