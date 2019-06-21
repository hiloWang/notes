package ztiany.chapter10;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class CallScript {

    public static void main(String... args) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine groovy = scriptEngineManager.getEngineByName("groovy");

        try {
            groovy.eval("println 'Hello from Groovy' ");

            //eval方法还有其他的重载版本可以使用，比如接受一个Reader

            //使用put可以向脚本中传递参数
            groovy.put("name", "Ztiany");
            groovy.eval("println \"Hello $name from Groovy \"");



        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}
