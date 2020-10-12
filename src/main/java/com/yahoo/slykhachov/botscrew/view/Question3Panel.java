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

public class Question3Panel extends JPanel implements Updatable {
    private static final long serialVersionUID = 1L;
    private static Logger LOGGER = LoggerFactory.getLogger(Question3Panel.class);
    private AnswerPanel answerPanel;
    private String departmentName;
    Question3Panel(Model model) {
        this.setLayout(new GridLayout(2, 1));
        answerPanel = new AnswerPanel();
        add(new InquiryPanel(model, this));
        add(answerPanel);
    }
    private void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    private String getDepartmentName() {
        return this.departmentName;
    }
    private static class InquiryPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        static final String question;
        static final String sqlTemplate;
        static {
            question = "Show the average salary for department";
            sqlTemplate = "SELECT AVG(salary) "
                + "FROM ("
                + "SELECT lector.salary, department_name "
                + "FROM ("
                + "SELECT * "
                + "FROM lector_department "
                + "WHERE department_name = ?"
                + ") AS subquery1 "
                + "JOIN lector "
                + "ON lector.lector_id = subquery1.lector_id"
                + ") AS subquery2";
        }
        private Model model;
        private JTextField textField;
        InquiryPanel(Model model, Question3Panel q3p) {
            this.model = model;
            JLabel label = new JLabel();
            label.setText(InquiryPanel.question);
            this.textField = new JTextField(20);
            JButton button = new JButton("Query");
            button.addActionListener(
                e -> {
                    q3p.setDepartmentName(this.textField.getText());
                    this.textField.setText("");
                    this.model.executeQuery(
                        InquiryPanel.sqlTemplate,
                        q3p.getDepartmentName()
                    );
                });
            this.add(label);
            this.add(textField);
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
            String answer = "<html>Answer:<br><br>The average salary for "
                + params[0] + " department is " + params[1] + "</html>";
            this.label.setText(answer);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    @Override
    public void post(ResultSet resultSet) {
        String averageSalary = "0";
        try {
            if (resultSet.next()) {
                averageSalary = resultSet.getString(1);
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
            averageSalary
        );
        this.answerPanel.repaint();
    }
}

