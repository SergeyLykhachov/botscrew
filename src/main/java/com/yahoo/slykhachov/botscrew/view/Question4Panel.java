package com.yahoo.slykhachov.botscrew.view;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.slykhachov.botscrew.model.Model;
import com.yahoo.slykhachov.botscrew.util.Updatable;

public class Question4Panel extends JPanel implements Updatable {
    private static final long serialVersionUID = 1L;
    private static Logger LOGGER = LoggerFactory.getLogger(Question4Panel.class);
    private AnswerPanel answerPanel;
    private String departmentName;
    Question4Panel(Model model) {
        this.setLayout(new GridLayout(2, 1));
        this.answerPanel = new AnswerPanel();
        this.add(new InquiryPanel(model, this));
        this.add(answerPanel);
    }
    private void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    private String getDepartmentName() {
        return this.departmentName;
    }
    private static class InquiryPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        static final String subQuestion1;
        static final String subQuestion2;
        static final String sqlTemplate;
        static {
            subQuestion1 = "Show employee count for";
            subQuestion2 = "department";
            sqlTemplate = "SELECT COUNT(*) "
                + "FROM lector_department "
                + "WHERE department_name = ?";
        }
        private Model model;
        private JTextField textField;
        InquiryPanel(Model model, Question4Panel q4p) {
            this.model = model;
            JLabel label1 = new JLabel();
            label1.setText(InquiryPanel.subQuestion1);
            this.textField = new JTextField(20);
            JLabel label2 = new JLabel();
            label2.setText(InquiryPanel.subQuestion2);
            JButton button = new JButton("Query");
            button.addActionListener(
                e -> {
                    q4p.setDepartmentName(this.textField.getText());
                    this.textField.setText("");
                    this.model.executeQuery(
                        InquiryPanel.sqlTemplate,
                        q4p.getDepartmentName()
                    );
                });
            this.add(label1);
            this.add(textField);
            this.add(label2);
            this.add(button);
            this.setBorder(
                new CompoundBorder(
                    new BevelBorder(BevelBorder.RAISED),
                    new EtchedBorder()
                )
            );
        }
    }
    private static class AnswerPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private JLabel label;
        AnswerPanel() {
            this.label = new JLabel();
            this.label.setText("Answer:");
            this.add(label);
            this.setBorder(
                new CompoundBorder(
                    new BevelBorder(BevelBorder.RAISED),
                    new EtchedBorder()
                )
            );
        }
        void setAnswer(String... params) {
            String answer = "<html>Answer:<br><br>"
                + params[1] + " employee(s) work in "
                + params[0] + " department</html>";
            this.label.setText(answer);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    @Override
    public void post(ResultSet resultSet) {
        String employeeCout = "0";
        try {
            if (resultSet.next()) {
                employeeCout = String.valueOf(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        this.answerPanel.setAnswer(
            this.getDepartmentName(),
            employeeCout
        );
        this.answerPanel.repaint();
    }
}


