package edu.byu.edge.jdbc.domain;

import java.math.BigDecimal;

/**
 * Created by wct5 on 5/9/16.
 */
public class Sequence {
	protected String sequenceOwner;
	protected String sequenceName;
	protected BigDecimal minValue;
	protected BigDecimal maxValue;
	protected int incrementBy;
	protected String cycleFlag;
	protected String orderFlag;
	protected int cacheSize;
	protected BigDecimal lastNumber;

	public Sequence() {
	}

	public Sequence(
			final String sequenceOwner, final String sequenceName, final BigDecimal minValue, final BigDecimal maxValue, final int incrementBy,
			final String cycleFlag, final String orderFlag, final int cacheSize, final BigDecimal lastNumber
	) {
		this.sequenceOwner = sequenceOwner;
		this.sequenceName = sequenceName;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.incrementBy = incrementBy;
		this.cycleFlag = cycleFlag;
		this.orderFlag = orderFlag;
		this.cacheSize = cacheSize;
		this.lastNumber = lastNumber;
	}

	public String getSequenceOwner() {
		return sequenceOwner;
	}

	public void setSequenceOwner(final String sequenceOwner) {
		this.sequenceOwner = sequenceOwner;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(final String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(final BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(final BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public int getIncrementBy() {
		return incrementBy;
	}

	public void setIncrementBy(final int incrementBy) {
		this.incrementBy = incrementBy;
	}

	public String getCycleFlag() {
		return cycleFlag;
	}

	public void setCycleFlag(final String cycleFlag) {
		this.cycleFlag = cycleFlag;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(final String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(final int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public BigDecimal getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(final BigDecimal lastNumber) {
		this.lastNumber = lastNumber;
	}
}
