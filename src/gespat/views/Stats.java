package views;

import components.Label;
import components.template.XSpaceBetween;
import controllers.ConsultationController;
import controllers.DeviceController;
import models.Device;
import net.miginfocom.swing.MigLayout;
import utils.Colors;
import views.popups.UserMessage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Stats extends JFrame {

	private static final long serialVersionUID = -1377226174334041975L;

	private ConsultationController consultCtrl;
	private DeviceController deviceCtrl;

	/**
	 * Création de la page contenant les statistiques
	 */
	public Stats(ConsultationController consultationController, DeviceController deviceController) {
		setTitle("GesPat — Statistiques");
		setSize(800, 575);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Colors.PRIMARY);

		consultCtrl = consultationController;
		deviceCtrl = deviceController;

		UserMessage.setActiveWindow(this);

		add(this.getContent());
		setVisible(true);
	}

	/**
	 * Ajout du contenu à l'interface
	 */
	private JPanel getContent() {
		// TODO: fix background
		JPanel right = new JPanel();
		right.setLayout(new MigLayout("wrap 1"));
		right.setBackground(Colors.PRIMARY);

		right.add(new Label("Patients par maladies", Label.Styles.TITLE));
		// TODO: Transformer avec un tableau
		for (DataStats stat : getDevicesStats()) {
			right.add(new Label(stat.getLabel() + " : " + stat.getValue()));
		}

		JPanel left = new JPanel();
		left.setLayout(new MigLayout());
		left.setBackground(Colors.PRIMARY);
		left.add(new Label("Nombre d'appareils par patients", Label.Styles.TITLE));

		return new XSpaceBetween(right, left);
	}

	/**
	 * @return les statistiques des pathologies
	 */
	private List<DataStats> getPathologiesStats() {
		List<DataStats> pathologiesStats = new ArrayList<>();

		// TODO: finir la méthode
		return pathologiesStats;
	}

	/**
	 * @return les statistiques des appareils
	 */
	private List<DataStats> getDevicesStats() {
		List<DataStats> devicesStats = new ArrayList<>();

		for (Device device : deviceCtrl.getAll()) {
			if (!device.getLabel().equals("null")) {
				DataStats s = new DataStats(device.getLabel());
				devicesStats.forEach(stat -> {
					// TODO: Fix this
					// System.out.println(stat.getLabel().equals(s.getLabel()));
					if (stat.getLabel().equals(s.getLabel())) {
						stat.increment();
					} else {
						devicesStats.add(s);
					}
				});
			}
		}

		return devicesStats;
	}

}

/**
 * Une donnée statistique à afficher dans un tableau
 */
final class DataStats {

	private String label;
	private int value;

	public DataStats(String label) {
		this.label = label;
		this.value = 1;
	}

	public int getValue(){
		return value;
	}

	public void setValue(int val){
		value=val;
	}

	public void increment() {
		value++;
	}

	public String getLabel(){
		return label;
	}

	public void setLabel(String string){
		label=string;
	}
}