package edu.byu.edge.jdbc.domain;

import java.util.List;

/**
 * Created by wct5 on 5/9/16.
 */
public class Schema {

	protected String name;
	protected List<Table> tables;
	protected List<Sequence> sequences;

	public Schema() {
	}

	public Schema(final String name, final List<Table> tables, final List<Sequence> sequences) {
		this.name = name;
		this.tables = tables;
		this.sequences = sequences;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(final List<Table> tables) {
		this.tables = tables;
	}

	public List<Sequence> getSequences() {
		return sequences;
	}

	public void setSequences(final List<Sequence> sequences) {
		this.sequences = sequences;
	}
}
