package br.ufc.great.syssu.base;

import br.ufc.great.syssu.base.interfaces.IFilter;

public class Query {
	
	private Pattern pattern;
	private IFilter javaFilter;
	private String jsFilter;
	
	public Query(Pattern pattern, String filter) {
		this.pattern = pattern;
		this.jsFilter = filter;
	}

	public Query(Pattern pattern, IFilter filter) {
		this.pattern = pattern;
		this.javaFilter = filter;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getJavaScriptFilter() {
		return jsFilter;
	}
	
	public IFilter getJavaFilter() {
		return javaFilter;
	}
}
