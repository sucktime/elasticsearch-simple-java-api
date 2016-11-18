package zy.engine.facet;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangGuofeng on 2016/10/21.
 */

public class SearchScorer {
    /**
     * Score mode defines how results of individual score functions will be aggregated.
     * Can be first, avg, max, sum, min, multiply
     */
    private String scoreMode = "multiply";
    /**
     * Score mode defines how the combined result of score functions will influence the final score together with the sub query score.
     * Can be replace, avg, max, sum, min, multiply
     */
    private String boostMode = "multiply";
    /**
     * set a list of score scipts, each will be warapped as a score function which generating a score
     * the score generated will be combined by scoreMode
     */
    private List<SearchScoreScript> scoreScripts = new LinkedList<SearchScoreScript>();

    public SearchScorer(){
    }

    public SearchScorer(List<SearchScoreScript> scoreScripts){
        this.scoreScripts = scoreScripts;
    }

    public SearchScorer addScoreScript(SearchScoreScript searchScoreScript){
        this.scoreScripts.add(searchScoreScript);
        return this;
    }

    public String getScoreMode() {
        return scoreMode;
    }

    public String getBoostMode() {
        return boostMode;
    }

    public List<SearchScoreScript> getScoreScripts() {
        return scoreScripts;
    }

    public SearchScorer setScoreMode(String scoreMode) {
        this.scoreMode = scoreMode;
        return this;
    }

    public SearchScorer setBoostMode(String boostMode) {
        this.boostMode = boostMode;
        return this;
    }

    public SearchScorer(SearchScoreScript scoreScript){
        this.addScoreScript(scoreScript);
    }

    public SearchScorer setScoreScripts(List<SearchScoreScript> scoreScripts) {
        this.scoreScripts = scoreScripts;
        return this;
    }

    public static SearchScorer searchScorer(){
        return new SearchScorer();
    }

    public static class SearchScoreScript {
        String script = null;
        Map params = null;

        public SearchScoreScript(String script, Map params) {
            this.script = script;
            this.params = params;
        }

        public String getScript() {
            return script;
        }

        public Map getParams() {
            return params;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public void setParams(Map params) {
            this.params = params;
        }

        public class ScriptTemplate {
            String tempalte = null;
            String[] filedNames = null;

            public ScriptTemplate(String tempalte, String[] filedNames) {
                this.tempalte = tempalte;
                this.filedNames = filedNames;
            }

            public String script() {
                return String.format(tempalte, filedNames);
            }
        }
    }
}
