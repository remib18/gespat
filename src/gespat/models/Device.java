package models;

public class Device extends AbstractData {

	/** États de l'attribution */
	public enum STATES {
		UNDEFINED, PENDING, ASSIGNED
	}

	private STATES state;
	private String label;

	/**
	 * Modèle de donnée représentant un appareil médical à attribué à un patient.
	 * Utiliser un <code>DeviceController</code> pour créer et enregistrer une consultation correctement.
	 *
	 * @param id    identifiant de l'appareil
	 * @param state état d'attribution
	 * @param label nom de l'appareil à afficher
	 * @see controllers.DeviceController
	 */
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
}