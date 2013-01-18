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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.nex.util.TextFileHandler;
/**
 * <p>Title: IBIS Server Tester</p>
 * <p>Description: Test Webservices with the IBIS Server</p>
 * <p>Copyright: Copyright (c) 2009, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @author Park
 * @version 1.0
 *
 * <p>TODO: we must open with a feature that lets user
 * authenticate and set baseURL when calling helper.init()
 * </p>
 */
public class UploadFormDialog
    extends JDialog {
  private MainFrame host;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel jLabel1 = new JLabel();
  JTextField fileNameField = new JTextField();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel jLabel2 = new JLabel();
  JTextField platformField = new JTextField();
  FlowLayout flowLayout3 = new FlowLayout();
  JLabel jLabel3 = new JLabel();
  JTextField descriptionField = new JTextField();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea cargoArea = new JTextArea();
  JPanel jPanel4 = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JButton submitButton = new JButton();
  JButton cancelButton = new JButton();
  JPanel jPanel5 = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  JPanel jPanel7 = new JPanel();
  JPanel jPanel8 = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  JTextField loginField = new JTextField();
  JLabel jLabel5 = new JLabel();
  FlowLayout flowLayout7 = new FlowLayout();
  JPasswordField passwordField = new JPasswordField();
  JLabel jLabel6 = new JLabel();
  JButton browseButton = new JButton();

  public UploadFormDialog() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(400,400);
    northPanel.setLayout(gridLayout1);
    gridLayout1.setRows(4);
    jPanel1.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    jLabel1.setText("File Name        ");
    fileNameField.setPreferredSize(new Dimension(300, 20));
    fileNameField.setText("");
    jPanel3.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    jLabel2.setText("Platform Code ");
    platformField.setPreferredSize(new Dimension(300, 20));
    platformField.setText("");
    jPanel2.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    jLabel3.setText("Description     ");
    descriptionField.setPreferredSize(new Dimension(300, 20));
    descriptionField.setText("");
    cargoArea.setText("");
    cargoArea.setLineWrap(true);
    jPanel4.setLayout(flowLayout4);
    submitButton.setText("Submit");
    submitButton.addActionListener(new
                                   UploadFormDialog_submitButton_actionAdapter(this));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new
                                   UploadFormDialog_cancelButton_actionAdapter(this));
    this.setTitle("File Upload Dialog");
    jPanel5.setLayout(gridLayout2);
    gridLayout2.setColumns(2);
    jPanel8.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    loginField.setPreferredSize(new Dimension(80, 20));
    loginField.setText("");
    jLabel5.setText("Login");
    jPanel7.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    passwordField.setPreferredSize(new Dimension(80, 22));
    passwordField.setText("jPasswordField1");
    jLabel6.setText("Pwd");
    browseButton.setToolTipText("Browse for a file to upload");
    browseButton.setText("Browse...");
    browseButton.addActionListener(new
                                   UploadFormDialog_browseButton_actionAdapter(this));
    northPanel.add(jPanel1);
    jPanel1.add(jLabel1);
    jPanel1.add(fileNameField);
    jPanel3.add(jLabel2);
    jPanel3.add(platformField);
    northPanel.add(jPanel2);
    jPanel2.add(jLabel3);
    northPanel.add(jPanel3);
    northPanel.add(jPanel5);
    jPanel5.add(jPanel8);
    jPanel8.add(jLabel5);
    jPanel8.add(loginField);
    jPanel5.add(jPanel7);
    jPanel7.add(jLabel6);
    jPanel7.add(passwordField);
    jPanel7.add(browseButton);
    jPanel2.add(descriptionField);
    this.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
    jScrollPane1.getViewport().add(cargoArea);
    this.getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);
    jPanel4.add(submitButton);
    jPanel4.add(cancelButton);

    this.getContentPane().add(northPanel, java.awt.BorderLayout.NORTH);
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
  public void setHost(MainFrame h) {
    host = h;
  }
  public void cancelButton_actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }

  public void submitButton_actionPerformed(ActionEvent e) {
    host.handleUpload(fileNameField.getText(),platformField.getText(),
                      descriptionField.getText(), cargoArea.getText(),
                      loginField.getText(),
                      new String(passwordField.getPassword()));
    this.setVisible(false);
  }

  public void browseButton_actionPerformed(ActionEvent e) {
    TextFileHandler h = new TextFileHandler();
    File f = h.openFile("Select a File");
    try {
      if (f != null) {
        fileNameField.setText(f.getName());
        String xml = h.readFile(f);
        // strip off leading FEFF
        if (((int)xml.charAt(0)== 254) && ((int)xml.charAt(1) == 255))
          xml = xml.substring(2);
        //System.out.println(xml);
        cargoArea.setText(xml);
      }
    } catch (Exception x) {
      x.printStackTrace();
    }
  }
}

class UploadFormDialog_browseButton_actionAdapter
    implements ActionListener {
  private UploadFormDialog adaptee;
  UploadFormDialog_browseButton_actionAdapter(UploadFormDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.browseButton_actionPerformed(e);
  }
}

class UploadFormDialog_submitButton_actionAdapter
    implements ActionListener {
  private UploadFormDialog adaptee;
  UploadFormDialog_submitButton_actionAdapter(UploadFormDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.submitButton_actionPerformed(e);
  }
}

class UploadFormDialog_cancelButton_actionAdapter
    implements ActionListener {
  private UploadFormDialog adaptee;
  UploadFormDialog_cancelButton_actionAdapter(UploadFormDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
