package org.dialywork.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import org.dialywork.ui.PopupMenu;
import org.dialywork.ui.swing.laf.SexyArrowButtonUI;

public class MenuButtonFactory {

    @Inject
    private PopupMenu popupMenu;

    @Inject
    private SexyArrowButtonUI arrowButtonUI;

    public JButton create() {
        final JPopupMenu menu = popupMenu.getPopupMenu();
        final JButton button = new JButton();
        button.setUI(arrowButtonUI);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.show(button, 0, button.getHeight());
            }
        });
        return button;
    }

}
