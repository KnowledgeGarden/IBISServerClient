/*
 *  Copyright (C) 2009 Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of IBIS Server, an open source project.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.nex.ibis.client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Title: IBIS Server Tester</p>
 * <p>Description: Test Webservices with the IBIS Server</p>
 * <p>Copyright: Copyright (c) 2009, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @author Park
 * @version 1.0
 */
public class FileRemoveDialog
    extends JDialog {
  MainFrame host;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel jLabel1 = new JLabel();
  JTextField nameField = new JTextField();
  JLabel jLabel2 = new JLabel();
  JPasswordField jPasswordField1 = new JPasswordField();
  JTable indexTable = new JTable();
  JButton cancelButton = new JButton();

  public FileRemoveDialog(MainFrame h) {
    host = h;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setTitle("File Remove Dialog");
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(600,600);
    nameField.setPreferredSize(new Dimension(80, 20));
    jLabel2.setText("Password");
    jPasswordField1.setPreferredSize(new Dimension(80, 22));
    jPasswordField1.setText("hello");
    indexTable.addMouseListener(new FileRemoveDialog_indexTable_mouseAdapter(this));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new
                                   FileRemoveDialog_cancelButton_actionAdapter(this));
    this.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
    nameField.setText("");
    jPanel1.add(jLabel1);
    jPanel1.add(nameField);
    jPanel1.add(jLabel2);
    jPanel1.add(jPasswordField1);
    jPanel1.add(cancelButton);
    jLabel1.setText("Login");
    jPanel1.setLayout(flowLayout1);
    this.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
    jScrollPane1.getViewport().add(indexTable);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    this.setLocation( (screenSize.width - frameSize.width) / 2,
                     (screenSize.height - frameSize.height) / 2);

  }


  public void indexTable_mouseClicked(MouseEvent e) {
    int which = indexTable.getSelectedRow();
    DefaultTableModel m = (DefaultTableModel)indexTable.getModel();
    Object o = m.getValueAt(which,0);
    String filename = (String)o;
    o = m.getValueAt(which,1);
    String platform = (String)o;
    System.out.println(filename+" "+platform);
    this.setVisible(false);
    host.removeDialogCallBack(filename,platform,
                              nameField.getText(),new String(jPasswordField1.getPassword()));
  }

  public void fillTable(java.util.List<java.util.List> files) {
    int len = files.size();
    DefaultTableModel model = new DefaultTableModel(0,4);
    for (int i=0;i<files.size();i++) {
      java.util.List x = files.get(i);
      java.util.Vector v = new java.util.Vector(3);
      v.addElement(x.get(0));
      v.addElement(x.get(1));
      v.addElement(x.get(3));
      v.addElement(x.get(2));
      model.addRow(v);
    }
    indexTable.setModel(model);
  }

  public void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }

}

class FileRemoveDialog_cancelButton_actionAdapter
    implements ActionListener {
  private FileRemoveDialog adaptee;
  FileRemoveDialog_cancelButton_actionAdapter(FileRemoveDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class FileRemoveDialog_indexTable_mouseAdapter
    extends MouseAdapter {
  private FileRemoveDialog adaptee;
  FileRemoveDialog_indexTable_mouseAdapter(FileRemoveDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.indexTable_mouseClicked(e);
  }
}
