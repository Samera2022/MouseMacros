package io.github.samera2022.mousemacros.app.script;

public class ScriptIssue {
    private final ScriptProblem problem;
    private final String[] args;

    public ScriptIssue(ScriptProblem problem, String[] args) {
        this.problem = problem;
        this.args = args;
    }

    public ScriptProblem getProblem() {
        return problem;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isSevere() {
        return problem.isSevere();
    }
}
