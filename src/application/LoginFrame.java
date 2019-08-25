package application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginFrame extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldDBName;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Login");
        setBounds(100, 100, 226, 247);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setBorder(
                new TitledBorder(null, "Logon to Twitch", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
                GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
                GroupLayout.PREFERRED_SIZE, 195, Short.MAX_VALUE));

        JLabel lblDatenbankname = new JLabel("Twitch-Username:");

        textFieldDBName = new JTextField();
        textFieldDBName.setColumns(10);

        JLabel lblBenutzername = new JLabel("Chanel ID:");

        textFieldUsername = new JTextField();
        textFieldUsername.setColumns(10);

        JLabel lblPasswort = new JLabel("OAuth ID:");

        passwordField = new JPasswordField();

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame.this.setVisible(false);
            }
        });
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(textFieldDBName, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addComponent(lblDatenbankname)
                                .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup()
                                .addComponent(lblBenutzername)
                                .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup()
                                .addComponent(lblPasswort)
                                .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup()
                                .addComponent(btnLogin)
                                .addPreferredGap(ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                                .addComponent(btnCancel))
                        .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                        .addComponent(textFieldUsername, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblDatenbankname)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textFieldDBName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(lblBenutzername)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(lblPasswort)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnLogin)
                                        .addComponent(btnCancel))
                                .addContainerGap(49, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        contentPane.setLayout(gl_contentPane);
    }

}
