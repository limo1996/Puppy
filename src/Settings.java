package research.analysis;

import java.io.*;
import soot.G;

class Settings {
    private int debug;
    private boolean testing;
    private String test_out;
    private FileWriter _stream;

    public Settings(int debug, String test_out){
        this.debug = debug;
        testing = test_out != null;
        this.test_out = test_out;

        if(testing){
            try {
                File file = new File(test_out);
                file.createNewFile();
                new PrintWriter(file).close();
                _stream = new FileWriter(file, true);
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
    }

    public void debug(String msg, int level, boolean newLine) {
        if(level >= debug) {
            G.v().out.print(newLine ? msg + "\n" : msg);
        }
    }

    public void test(String msg, boolean newLine) {
        if(testing){
            assert _stream != null : "Stream null but testing?";
            try {
                _stream.write(newLine ? msg + "\n" : msg);
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
    }

    public void testEnd() {
        if(_stream != null) {
            try {
                _stream.flush();
                _stream.close();
            } catch (Exception ex) {
                debug(ex.toString(), 3, true);
            }
        }
    }

}