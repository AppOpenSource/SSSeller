package com.abt.seller.utils;

@SuppressWarnings("rawtypes")
public class QParameter implements java.io.Serializable, Comparable {
	private static final long serialVersionUID = 5164951358145483848L;
	protected String name;
	protected String value;

	public QParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public boolean equals(Object arg0) {
		if (null == arg0) {
			return false;
		}
		if (this == arg0) {
			return true;
		}
		if (arg0 instanceof QParameter) {
			QParameter param = (QParameter) arg0;

			return this.name.equals(param.name)
					&& this.value.equals(param.value);
		}
		return false;
	}

	public int compareTo(Object o) {
		int compared;
		QParameter param = (QParameter) o;
		compared = name.compareTo(param.name);
		if (0 == compared) {
			compared = value.compareTo(param.value);
		}
		return compared;
	}
}