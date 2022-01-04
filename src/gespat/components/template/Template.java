package components.template;

import components.search.FilterInterface;
import components.search.Search;
import components.search.SearchBar;
import components.search.SearchSelectedListener;
import components.table.AbstractTableModel;
import components.table.Table;
import components.Label;
import components.Label.Styles;
import models.Data;
import utils.Colors;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Template<T extends Data> extends JPanel implements ComponentListener {

    /**
     * Différentes positions du template
     */
    public enum In {
        MAIN_HEADER, MAIN_BODY,
        SIDEBAR_HEADER, SIDEBAR_BODY, SIDEBAR_FOOTER
    }

    private static final long serialVersionUID = -4176427088749241304L;
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private int panelWidth;
    private int panelHeight;

    private int sidebarWidth;

    private final JPanel main = new JPanel();
    private final JPanel header = new JPanel();
    private final JPanel body = new JPanel();

    private final JPanel sidebar = new JPanel();
    private final JPanel sidebarHeader = new JPanel();
    private final JPanel sidebarBody = new JPanel();
    private final XCenteredContainer sidebarFooter = new XCenteredContainer(Colors.SECONDARY);
    private final SearchBar searchBar = new SearchBar();
    private Search<T> search;

    /**
     * Construit une fenêtre responsive.
     *
     * @param width  - taille initiale de la fenêtre
     * @param height - taille initiale de la fenêtre
     */
    public Template(int width, int height) {
        panelWidth = width;
        panelHeight = height;
        sidebarWidth = panelWidth * 2 / 7;

        addComponentListener(this);
        init();

        // Permet le reset et la résolution de bugs d'affichage
        EventQueue.invokeLater(() -> resize(this));
    }

    /**
     * @apiNote Unsupported
     * @see Template#add(Component, In)
     * @deprecated (1.0, , change to add ( Component, TemplatePositions))
     */
    @SuppressWarnings("GrazieInspection")
    @Deprecated
    @Override
    public Component add(Component comp) {
        logger.log(Level.SEVERE, "Implémentation non supportée.");
        return null;
    }

    /**
     * Ajoute un élément à la position indiqué et le retourne.
     *
     * @param comp composant à ajouter
     * @param pos  position où insérer le composant
     * @return le composant fournit
     * @see In pour les différentes options de positionnement
     */
    public Component add(Component comp, In pos) {
        switch (pos) {
            case MAIN_HEADER:
                header.add(comp, "gapleft 16");
                break;

            case MAIN_BODY:
                body.add(comp);
                break;

            case SIDEBAR_HEADER:
                ((Container) sidebarHeader.getComponent(1)).add(comp);
                break;

            case SIDEBAR_BODY:
                sidebarBody.add(comp);
                break;

            case SIDEBAR_FOOTER:
                sidebarFooter.add(comp);
                break;

            default:
                logger.log(Level.SEVERE, "Position inhéxistante.");
                break;
        }
        return this;
    }

    /**
     * Ajoute un élément à la position indiqué et le retourne.
     *
     * @param comp   composant à ajouter
     * @param pos    position où insérer le composant
     * @param params paramètres de disposition
     * @return le composant fournit
     * @see In pour les différentes options de positionnement
     */
    public Component add(Component comp, In pos, String params) {
        switch (pos) {
            case MAIN_HEADER:
                header.add(comp, params);
                break;

            case MAIN_BODY:
                body.add(comp, params);
                break;

            case SIDEBAR_BODY:
                sidebarBody.add(comp, params);
                break;

            case SIDEBAR_FOOTER:
                sidebarFooter.add(comp, params);
                break;

            default:
                logger.log(Level.SEVERE, "Position inhéxistante.");
                break;
        }
        return this;
    }

    public void clear(In panel) {
        switch (panel) {
            case SIDEBAR_BODY:
                sidebarBody.removeAll();
                break;

            case SIDEBAR_FOOTER:
                sidebarFooter.removeAll();
                break;

            default:
                logger.log(Level.SEVERE, "Méthode non implémentée.");
                break;
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Permet un comportement responsive.
     */
    @Override
    public void componentResized(ComponentEvent e) {
        resize(e.getComponent());
    }

    private void resize(Component cp) {
        panelWidth = cp.getWidth();
        panelHeight = cp.getHeight();
        sidebarWidth = panelWidth * 2 / 7;

        main.setPreferredSize(new Dimension(panelWidth - sidebarWidth, panelHeight));

        Dimension sidebarDim = new Dimension(sidebarWidth, panelHeight);
        sidebar.setPreferredSize(sidebarDim);
        sidebar.setMaximumSize(sidebarDim);
        sidebar.setMinimumSize(sidebarDim);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Effectue un recalcul de l'affichage de la sidebar
     * Notamment utile si des composants sont ajouter ou supprimer
     */
    public void updateGUI() {
        search.update();
        repaint();
        revalidate();
        String s = searchBar.getText();
        searchBar.setText("  ");
        searchBar.setText(s);
        repaint();
        revalidate();

    }

    /**
     * ConstrColorst le layout
     */
    private void init() {
        // Attribution des LayoutManager
        header.setLayout(new MigLayout("", "", "c"));
        body.setLayout(new MigLayout("wrap 1"));
        sidebarHeader.setLayout(new BorderLayout());
        sidebarBody.setLayout(new MigLayout("wrap 1"));

        // Génération des deux conteneurs de contenu principaux
        initMain();
        initSidebar();

        // Création du conteneur général
        setLayout(new MigLayout("insets 0"));
        setBackground(Colors.PRIMARY);
        super.add(this.main, "gapright 0");
        super.add(this.sidebar);
    }

    /**
     * Construit et retourne le panel main
     */
    private void initMain() {
        // Attribution du layout
        main.setLayout(new BorderLayout());

        // Redimentionnement
        main.setPreferredSize(new Dimension(panelWidth - sidebarWidth, panelHeight));

        // Changement des couleurs
        main.setBackground(Colors.PRIMARY);
        header.setBackground(Colors.PRIMARY);
        body.setBackground(Colors.PRIMARY);

        // Insertion des containers enfants
        main.add(header, BorderLayout.NORTH);
        main.add(body);

    }

    /**
     * Construit et retourne le panel sidebar
     */
    private void initSidebar() {
        // Attribution du layout
        sidebar.setLayout(new BorderLayout());

        // Redimentionnement
        Dimension sDim = new Dimension(sidebarWidth, panelHeight);
        sidebar.setPreferredSize(sDim);
        sidebar.setMinimumSize(sDim);

        // Changement des couleurs
        sidebar.setBackground(Colors.SECONDARY);
        sidebarHeader.setBackground(Colors.SECONDARY);
        sidebarBody.setBackground(Colors.SECONDARY);
        sidebarFooter.setBackground(Colors.SECONDARY);

        // Insertion des containers enfants
        sidebar.add(sidebarHeader, BorderLayout.NORTH);
        sidebar.add(sidebarBody);
        sidebar.add(sidebarFooter, BorderLayout.SOUTH);

        // Attribution des donnés générales
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new MigLayout("", "", "c"));
        titleContainer.setOpaque(false);
        titleContainer.add(new Label("Détails", Styles.TITLE, Colors.TEXT_ON_SECONDARY));
        sidebarHeader.add(titleContainer, BorderLayout.WEST);

        JPanel exportContainer = new JPanel();
        exportContainer.setLayout(new MigLayout("", "", "c"));
        exportContainer.setBackground(Colors.SECONDARY);
        sidebarHeader.add(exportContainer, BorderLayout.EAST);
    }

    /**
     * Ajoute au template la barre de recherche et le tableau des résultats.
     *
     * @param data     correspond aux donnés parmis lesquels effectuer la recherche
     * @param model    correspond au model de tableau
     * @param listener l'écouteur à placer sur la recherche
     */
    public void setSearchBar(List<T> data, AbstractTableModel<T> model, SearchSelectedListener<T> listener) {
        setSearchBar(data, model, listener, new ArrayList<>());
    }

    /**
     * Ajoute au template la barre de recherche et le tableau des résultats.
     *
     * @param data     correspond aux donnés parmis lesquels effectuer la recherche
     * @param model    correspond au model de tableau
     * @param listener l'écouteur à placer sur la recherche
     * @param filters  l'ensemble des filtres à appliquer
     */
    public void setSearchBar(List<T> data, AbstractTableModel<T> model, SearchSelectedListener<T> listener,
                             List<FilterInterface<T>> filters) {
        search = new Search<>(
                searchBar,
                data,
                new Table<>(model));
        search.addSelectionListener(listener);
        for (FilterInterface<T> filter : filters) {
            search.addFilter(filter);
        }
        searchBar.addSearchListener(search);
        header.add(searchBar);
    }

    /**
     * Sélectionne la ligne
     *
     * @param data ligne à selectionner
     */
    public void setResultTableSelectedRow(T data) {
        search.setSelected(data);
    }

    /**
     * Sélectionne la ligne
     *
     * @param row ligne à selectionner
     */
    public void setResultTableSelectedRow(int row) {
        search.setSelected(row);
    }
}