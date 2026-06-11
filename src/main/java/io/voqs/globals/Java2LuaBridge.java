package io.voqs.globals;

public class Java2LuaBridge {
    public class Logger {

        public static void Notify(String text) {
            System.out.println("[INFO] " + text);
        }

        public static void Error(String cause, String text){
            System.out.printf("[ERR] %s :: %s%n", cause, text);
        }

        public static void Warn(String cause, String text){
            System.out.printf("[WARN] %s :: %s%n", cause, text);
        }
    }
}
