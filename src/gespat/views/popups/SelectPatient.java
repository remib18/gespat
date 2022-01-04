package views.popups;

import models.Patient;
import utils.DataTransmitterInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectPatient {

    private final JPopupMenu popupMenu = new JPopupMenu("Patients");
    private final List<Patient> data;
    private final List<DataTransmitterInterface<Patient>> listeners = new ArrayList<>();

    public SelectPatient(List<Patient> data, Container container, int x, int y) {
        this.data = data;

        initPopup();
        popupMenu.show(container, x, y);
    }

    private void initPopup() {
        for (Patient patient : data) {
            JMenuItem item = new JMenuItem(patient.getFullname());
            item.addActionListener(e -> publish(patient));
            popupMenu.add(item);
        }
    }

    public void subscribe(DataTransmitterInterface<Patient> listener) {
        listeners.add(listener);
    }

    private void publish(Patient patient) {
        listeners.forEach(l -> l.transmit(patient));
    }
}
