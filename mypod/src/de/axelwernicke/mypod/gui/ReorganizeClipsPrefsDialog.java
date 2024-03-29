// ReorganizeClipsPrefsDialog
// $Id: ReorganizeClipsPrefsDialog.java,v 1.8 2003/02/03 19:07:04 axelwernicke Exp $
//
// Copyright (C) 2002-2003 Axel Wernicke <axel.wernicke@gmx.de>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package de.axelwernicke.mypod.gui;

import javax.swing.JFileChooser;

/**
 * @author axel wernicke
 */
public class ReorganizeClipsPrefsDialog extends javax.swing.JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean confirmed = false;

    private String dirStructure[] = { GuiUtils.getStringLocalized("resource/language", "leaveAsIs"), GuiUtils.getStringLocalized("resource/language", "artist"),
            GuiUtils.getStringLocalized("resource/language", "artistAlbum"), GuiUtils.getStringLocalized("resource/language", "genre"),
            GuiUtils.getStringLocalized("resource/language", "genreAlbum") };

    private String filenameStructure[] = { GuiUtils.getStringLocalized("resource/language", "leaveAsIs"), GuiUtils.getStringLocalized("resource/language", "trackClipname") };

    /** Creates new form ReorganizeClipsPrefsDialog */
    public ReorganizeClipsPrefsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(okButton);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
     * Editor.
     */
    private void initComponents()// GEN-BEGIN:initComponents
    {
        java.awt.GridBagConstraints gridBagConstraints;

        baseDirLabel = new javax.swing.JLabel();
        baseDirTextField = new javax.swing.JTextField();
        baseDirButton = new javax.swing.JButton();
        dirStructLabel = new javax.swing.JLabel();
        dirStructComboBox = new javax.swing.JComboBox();
        filenameStructLabel = new javax.swing.JLabel();
        filenameStructComboBox = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        removeEmptyDirsCheckBox = new javax.swing.JCheckBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle(GuiUtils.getStringLocalized("resource/language", "reorganizeClips"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("reorganizeClipsPrefsDialog");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        baseDirLabel.setForeground(new java.awt.Color(102, 102, 153));
        baseDirLabel.setText(GuiUtils.getStringLocalized("resource/language", "baseDirectory:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 0);
        getContentPane().add(baseDirLabel, gridBagConstraints);

        baseDirTextField.setEditable(false);
        baseDirTextField.setMinimumSize(new java.awt.Dimension(100, 20));
        baseDirTextField.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(12, 11, 0, 0);
        getContentPane().add(baseDirTextField, gridBagConstraints);

        baseDirButton.setText("...");
        baseDirButton.setPreferredSize(new java.awt.Dimension(30, 20));
        baseDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseDirButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(12, 2, 0, 12);
        getContentPane().add(baseDirButton, gridBagConstraints);

        dirStructLabel.setForeground(new java.awt.Color(102, 102, 153));
        dirStructLabel.setText(GuiUtils.getStringLocalized("resource/language", "directoryStructure:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 12, 0, 0);
        getContentPane().add(dirStructLabel, gridBagConstraints);

        dirStructComboBox.setFont(new java.awt.Font("Dialog", 1, 10));
        dirStructComboBox.setModel(new javax.swing.DefaultComboBoxModel(this.dirStructure));
        dirStructComboBox.setMinimumSize(new java.awt.Dimension(31, 20));
        dirStructComboBox.setPreferredSize(new java.awt.Dimension(31, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 12);
        getContentPane().add(dirStructComboBox, gridBagConstraints);

        filenameStructLabel.setForeground(new java.awt.Color(102, 102, 153));
        filenameStructLabel.setText(GuiUtils.getStringLocalized("resource/language", "filenameStructure:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 12, 0, 0);
        getContentPane().add(filenameStructLabel, gridBagConstraints);

        filenameStructComboBox.setFont(new java.awt.Font("Dialog", 1, 10));
        filenameStructComboBox.setModel(new javax.swing.DefaultComboBoxModel(this.filenameStructure));
        filenameStructComboBox.setMinimumSize(new java.awt.Dimension(31, 20));
        filenameStructComboBox.setPreferredSize(new java.awt.Dimension(31, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 12);
        getContentPane().add(filenameStructComboBox, gridBagConstraints);

        okButton.setText(GuiUtils.getStringLocalized("resource/language", "ok"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(okButton);

        cancelButton.setText(GuiUtils.getStringLocalized("resource/language", "cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(17, 12, 12, 12);
        getContentPane().add(buttonPanel, gridBagConstraints);

        removeEmptyDirsCheckBox.setSelected(true);
        removeEmptyDirsCheckBox.setText(GuiUtils.getStringLocalized("resource/language", "removeEmptyDirectories"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 0, 12);
        getContentPane().add(removeEmptyDirsCheckBox, gridBagConstraints);

        pack();
    }// GEN-END:initComponents

    private void baseDirButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_baseDirButtonActionPerformed
    {// GEN-HEADEREND:event_baseDirButtonActionPerformed
        // get a file selection dialog
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(GuiUtils.getStringLocalized("resource/language", "selectDirectoryToReorganize"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        int result = fc.showOpenDialog(this);

        // set selection (if any) to text field
        if (result == JFileChooser.APPROVE_OPTION) {
            this.baseDirTextField.setText(fc.getSelectedFile().getPath());
        }
    }// GEN-LAST:event_baseDirButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_cancelButtonActionPerformed
    {// GEN-HEADEREND:event_cancelButtonActionPerformed
        confirmed = false;
        setVisible(false);
        dispose();
    }// GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {// GEN-HEADEREND:event_okButtonActionPerformed
        confirmed = true;
        setVisible(false);
    }// GEN-LAST:event_okButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_closeDialog
        confirmed = false;
        setVisible(false);
        dispose();
    }// GEN-LAST:event_closeDialog

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isRemoveEmptyDirectories() {
        return this.removeEmptyDirsCheckBox.isSelected();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox removeEmptyDirsCheckBox;

    public javax.swing.JComboBox filenameStructComboBox;

    public javax.swing.JComboBox dirStructComboBox;

    private javax.swing.JButton okButton;

    private javax.swing.JLabel filenameStructLabel;

    private javax.swing.JButton baseDirButton;

    private javax.swing.JButton cancelButton;

    public javax.swing.JTextField baseDirTextField;

    private javax.swing.JLabel baseDirLabel;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JLabel dirStructLabel;
    // End of variables declaration//GEN-END:variables

}
