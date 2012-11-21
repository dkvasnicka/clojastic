
package net.danielkvasnicka.elasticsearch.plugin.clojure;

import net.danielkvasnicka.elasticsearch.scriptingengine.ClojureScriptEngineService;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.script.ScriptModule;

/**
 *
 */
public class ClojurePlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "lang-clojure";
    }

    @Override
    public String description() {
        return "A plugin allowing Clojure scripting support";
    }

    public void onModule(ScriptModule module) {
        module.addScriptEngine(ClojureScriptEngineService.class);
    }
}