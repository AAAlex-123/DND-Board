package alexman.dndboard.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
class ApplicationMenu extends JMenuBar {

	private final Application context;

	private final JMenu m_file, m_area, m_character;
	private final JMenuItem f_open, f_save, f_save_as, r_switch, c_add, c_remove, c_move;
	private final Action a_open, a_save, a_save_as, a_switch, a_add, a_remove, a_move;

	{
		a_open = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.OPEN.context(context).adjustRequirements();
				Application.Actions.OPEN.execute();
			}
		};

		a_save = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.SAVE.context(context).execute();
			}
		};

		a_save_as = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.SAVE_AS.context(context).execute();
			}
		};

		a_switch = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.SWITCH_TO_AREA.context(context).adjustRequirements();
				Application.Actions.SWITCH_TO_AREA.execute();
			}
		};

		a_add = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.ADD_CHARACTER.context(context).adjustRequirements();
				Application.Actions.ADD_CHARACTER.execute();
			}
		};

		a_remove = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.REMOVE_CHARACTER.context(context).adjustRequirements();
				Application.Actions.REMOVE_CHARACTER.execute();
			}
		};

		a_move = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Application.Actions.MOVE_CHARACTER_TO_AREA.context(context).adjustRequirements();
				Application.Actions.MOVE_CHARACTER_TO_AREA.execute();
			}
		};
	}

	public ApplicationMenu(Application context) {
		this.context = context;

		// --- file ---
		m_file = new JMenu("File");
		f_open = new JMenuItem("Open");
		f_save = new JMenuItem("Save");
		f_save_as = new JMenuItem("Save As");

		m_file.add(f_open);
		m_file.add(f_save);
		m_file.add(f_save_as);

		add(m_file);

		// --- area ---
		m_area = new JMenu("Area");
		r_switch = new JMenuItem("Switch");

		m_area.add(r_switch);

		add(m_area);

		// --- character ---
		m_character = new JMenu("Character");
		c_add = new JMenuItem("Add");
		c_remove = new JMenuItem("Remove");
		c_move = new JMenuItem("Move");

		m_character.add(c_add);
		m_character.add(c_remove);
		m_character.add(c_move);

		add(m_character);

		// make menus usable
		listeners();
		mnemonics();
		accelerators();
		icons();
	}

	private void listeners() {
		f_open.addActionListener(a_open);
		f_save.addActionListener(a_save);
		f_save_as.addActionListener(a_save_as);
		r_switch.addActionListener(a_switch);
		c_add.addActionListener(a_add);
		c_remove.addActionListener(a_remove);
		c_move.addActionListener(a_move);
	}

	private void mnemonics() {
		m_file.setMnemonic('F');
		m_area.setMnemonic('A');
		m_character.setMnemonic('C');
	}

	private void accelerators() {
		f_open.setAccelerator(KeyStroke.getKeyStroke("control O"));
		f_save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		f_save_as.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		r_switch.setAccelerator(KeyStroke.getKeyStroke("control E"));
		c_add.setAccelerator(KeyStroke.getKeyStroke("control A"));
		c_remove.setAccelerator(KeyStroke.getKeyStroke("control D"));
		c_move.setAccelerator(KeyStroke.getKeyStroke("control M"));
	}

	private void icons() {
		// m_file.setIcon(...);
	}
}
