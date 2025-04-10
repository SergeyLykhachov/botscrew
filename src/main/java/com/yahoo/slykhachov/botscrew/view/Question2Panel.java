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

public class Question2Panel extends JPanel implements Updatable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Question2Panel.class);
    private final AnswerPanel answerPanel;
    private String departmentName;

    Question2Panel(Model model) {
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
            subQuestion1 = "Show";
            subQuestion2 = "statistics";
            sqlTemplate = "SELECT degree, COUNT(*) "
                + "FROM ("
                + "SELECT * "
                + "FROM ("
                + "SELECT degree, department_name "
                + "FROM ("
                + "SELECT lector.lector_id, degree.degree "
                + "FROM lector "
                + "JOIN degree "
                + "ON lector.degree_id = degree.degree_id"
                + ") AS subquery1 "
                + "JOIN lector_department "
                + "ON subquery1.lector_id = lector_department.lector_id"
                + ") AS subquery2 "
                + "WHERE department_name = ?"
                + ") AS subquery3 "
                + "GROUP BY degree";
        }
        private final Model model;
        private final JTextField textField;

        InquiryPanel(Model model, Question2Panel q2p) {
            this.model = model;
            JLabel label1 = new JLabel();
            label1.setText(InquiryPanel.subQuestion1);
            this.textField = new JTextField(20);
            JLabel label2 = new JLabel();
            label2.setText(InquiryPanel.subQuestion2);
            JButton button = new JButton("Query");
            button.addActionListener(
                e -> {
                    q2p.setDepartmentName(this.textField.getText());
                    this.textField.setText("");
                    this.model.executeQuery(
                        InquiryPanel.sqlTemplate,
                        q2p.getDepartmentName()
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
        private final JLabel label;

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
            String answer = "<html>Answer:<br><br>Assistants - "
                + params[0] + "<br>Associate professors - "
                + params[1] + "<br>Professors - " + params[2] + "</html>";
            this.label.setText(answer);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    @Override
    public void post(ResultSet resultSet) {
        String assistant = "0";
        String associateProfessor = "0";
        String professor = "0";
        try {
            while (resultSet.next()) {
                String temp = resultSet.getString(1);
                if (temp.equalsIgnoreCase("assistant")) {
                    assistant = String.valueOf(resultSet.getInt(2));
                } else {
                    if (temp.equalsIgnoreCase("associate professor")) {
                        associateProfessor = String.valueOf(resultSet.getInt(2));
                    } else {
                        professor = String.valueOf(resultSet.getInt(2));
                    }
                }
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
            assistant,
            associateProfessor,
            professor
        );
        this.answerPanel.repaint();
    }
}

