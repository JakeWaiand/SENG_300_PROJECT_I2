/*
 * @author Enioluwafe Balogun - 30174298
 * @author Rebecca Hamshaw - 30191086
 * @author Eric George - 30173268
 * @author Khadeeja Abbas - 30180776
 * @author Mann Patel - 30182233
 */
package com.thelocalmarketplace.software;

public class State {

    private static boolean inSession = false;

    public static void setSession(boolean session) {
        inSession = session;
    }

    public static boolean isSession() {
        return inSession;
    }

    private State() {
    }

    
}
