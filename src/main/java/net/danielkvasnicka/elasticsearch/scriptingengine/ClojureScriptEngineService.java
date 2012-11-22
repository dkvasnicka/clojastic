/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package net.danielkvasnicka.elasticsearch.scriptingengine;

import clojure.lang.*;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptEngineService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * SEService for Clojure
 *
 * @author dkvasnicka
 */
public class ClojureScriptEngineService extends AbstractComponent implements ScriptEngineService {

    /**
     * Namespace and fn name of the function that performs
     * script initialization.
     */
    private static final String CLOJURE_UTIL_NS =
            "net.danielkvasnicka.elasticsearch.scriptingengine.utils";
    private static final String CLOJURE_UTIL_FN_NAME = "resolve-fn-from-script";

    @Inject
    public ClojureScriptEngineService(Settings settings) {
        super(settings);

        // load the util script
        try {
            RT.loadResourceScript("utils.clj");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] types() {
        return new String[] { "clojure", "clj" };
    }

    @Override
    public String[] extensions() {
        return new String[] { "clj" };
    }

    @Override
    public Object compile(String script) {
        // load the script
        clojure.lang.Compiler.load(new StringReader(script));
        // find the namespace and name of the first fn and resolve tne Var
        Object compiledScript = RT.var(CLOJURE_UTIL_NS, CLOJURE_UTIL_FN_NAME).invoke(script);

        if (compiledScript == null) {
            throw new IllegalStateException("Could not find a function in this script: " + script);
        }

        return compiledScript;
    }

    @Override
    public ExecutableScript executable(Object compiledScript, Map<String, Object> vars) {
        return new ExecutableClojureScript((Var) compiledScript, vars);
    }

    @Override
    public SearchScript search(Object compiledScript, SearchLookup lookup, @Nullable Map<String, Object> vars) {
        return new ClojureSearchScript((Var) compiledScript, lookup, vars);
    }

    @Override
    public Object execute(Object compiledScript, Map<String, Object> vars) {
        return new ExecutableClojureScript((Var) compiledScript, vars).run();
    }

    @Override
    public Object unwrap(Object value) {
        return value;
    }

    @Override
    public void close() {
    }

}