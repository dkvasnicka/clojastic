package net.danielkvasnicka.elasticsearch.scriptingengine;

import clojure.lang.Var;
import org.elasticsearch.script.ExecutableScript;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dkvasnicka
 * Date: 11/20/12
 * Time: 16:14
 */
public class ExecutableClojureScript implements ExecutableScript {

    protected Map<String, Object> vars;

    private Var compiledScript;

    public ExecutableClojureScript(Var compiledScript, Map<String, Object> vars) {
        assert compiledScript != null;
        this.vars = vars;
        this.compiledScript = compiledScript;
        if (this.vars == null) {
            this.vars = new HashMap<String, Object>();
        }
    }

    @Override
    public void setNextVar(String name, Object value) {
        this.vars.put(name, value);
    }

    @Override
    public Object run() {
        return this.compiledScript.invoke(this.vars);
    }

    @Override
    public Object unwrap(Object value) {
        return value;
    }
}
