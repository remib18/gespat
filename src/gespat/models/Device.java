package models;

public class Device extends AbstractData {

	private STATES state;
	private String label;

	public Device(final int id, final STATES state, final String label) {
		this.id = id;
		this.state = state;
		this.label = label;
	}

	@Override
	public String getSearchableFields() {
		return label;
	}

	@Override
	public String toString() {
		return id + fs +
				state + fs +
				label;
	}

	public STATES getState() {
		return state;
	}

	public void setState(final STATES state) {
		this.state = state;
	}

	public void setState(final Boolean state) {
		if (state) {
			setState(STATES.ASSIGNED);
			return;
		}
		if (state == null) {
			setState(STATES.UNDEFINED);
			return;
		}
		setState(STATES.PENDING);

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}


	public enum STATES {
		UNDEFINED, PENDING, ASSIGNED
	}
}