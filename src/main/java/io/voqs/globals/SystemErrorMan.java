package io.voqs.globals;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class SystemErrorMan {
    public static void error(String cause, String message){
        System.out.printf("[SYSTEM] %s : %s%n", cause, message);
    }

    public static class LuaErrors {
        public LuaErrors() {
        }

        public static void insufficientArgs(int count){
            error("Insufficient Arguments for Lua", String.format("Not enough arguments, Required %d", count));
        }

        public static void invalidArg(LuaValue value){
            error("Invalid Argument for Lua", String.format("Invalid argument '%s'.", value.tojstring()));
        }

        public static boolean checkInvalidArgs(LuaValue[] values, int... types) {
            if (values.length != types.length) return true;

            for (int i = 0; i < values.length; i++) {
                int type = types[i];
                LuaValue val = values[i];

                if (val.type() != type) {
                    invalidArg(val);
                    return true;
                }
            }

            return false;
        }

        public static boolean checkInsufficientArgs(Varargs args, int narg){
            if (args.narg() != narg){
                insufficientArgs(narg);
                return true;
            }

            return false;
        }
    }
}
